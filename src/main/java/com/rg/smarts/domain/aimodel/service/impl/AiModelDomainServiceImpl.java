package com.rg.smarts.domain.aimodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.helper.LLMContext;
import com.rg.smarts.domain.aimodel.model.AssistantChatParams;
import com.rg.smarts.domain.aimodel.model.LLMBuilderProperties;
import com.rg.smarts.domain.aimodel.model.SseAskParams;
import com.rg.smarts.domain.aimodel.repository.AiModelRepository;
import com.rg.smarts.domain.aimodel.service.AbstractLLMService;
import com.rg.smarts.domain.aimodel.service.AiModelDomainService;
import com.rg.smarts.domain.aimodel.service.adi.AiModelSettingService;
import com.rg.smarts.domain.aimodel.service.IChatAssistant;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.manager.RedisLimiterManager;
import com.rg.smarts.infrastructure.utils.BaseTools;
import com.rg.smarts.interfaces.vo.ai.ChatVO;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

/**
 * @author czr
 * @description 针对表【AiModel(AiModel表)】的数据库操作Service实现
 */
@Service
@Slf4j
public class AiModelDomainServiceImpl implements AiModelDomainService {
    @Resource
    private  AiModelRepository aiModelRepository;
    @Resource
    private ChatMemoryStore chatMemoryStore;
    @Resource
    private AiModelSettingService aiModelSettingService;
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Resource
    private BaseTools baseTools;

    @Override
    public AiModel addOne(AiModel aiModel) {
        LambdaQueryWrapper<AiModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AiModel::getName, aiModel.getName())
                .eq(AiModel::getPlatform, aiModel.getPlatform());
        long count = aiModelRepository.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.A_MODEL_ALREADY_EXIST);
        }
        aiModelRepository.save((aiModel));
        aiModelSettingService.addOrUpdate(aiModel);
        return aiModel;
    }
    @Override
    public AiModel getAiModelByIdOrThrow(Long id) {
        AiModel existModel = aiModelRepository.getById(id);
        if (null == existModel) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return existModel;
    }
    @Override
    public Boolean updateAiModel(AiModel aiModel) {
        AiModel oldAiModel = getAiModelByIdOrThrow(aiModel.getId());
        aiModelRepository.updateById(aiModel);
        AiModel updatedOne = getAiModelByIdOrThrow(oldAiModel.getId());
        aiModelSettingService.delete(oldAiModel);
        aiModelSettingService.addOrUpdate(updatedOne);
        return true;
    }
    public void disable(Long id) {
        AiModel model = new AiModel();
        model.setId(id);
        model.setIsEnable(0);
        aiModelRepository.updateById(model);
    }

    public void enable(Long id) {
        AiModel model = new AiModel();
        model.setId(id);
        model.setIsEnable(1);
        aiModelRepository.updateById(model);
    }


    /**
     * 普通聊天，将原始的用户问题及历史消息发送给AI
     */
    @Override
    public void commonChat(SseAskParams params) {
        redisLimiterManager.doRateLimit("chat_" + params.getUserId());
        AbstractLLMService llmService = LLMContext.getLLMServiceByName(params.getModelName());
        AssistantChatParams assistantChatParams = params.getAssistantChatParams();
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(50)
                .chatMemoryStore(chatMemoryStore)
                .build();
        IChatAssistant assistant = AiServices.builder(IChatAssistant.class)
                .streamingChatLanguageModel(llmService.buildStreamingChatLLM(params.getLlmBuilderProperties()))
                .chatMemoryProvider(chatMemoryProvider)
                .tools(baseTools)
                .build();
        TokenStream tokenStream = assistant.chatStream(assistantChatParams.getContext(), assistantChatParams.getMemoryId());
        tokenStream.onNext((String token) -> {
                    System.out.println(token);
                    try {
                        if (token != null) {
                            ChatVO chatVO = new ChatVO();
                            chatVO.setChatResponse(token);
                            chatVO.setId(params.getAssistantChatParams().getMemoryId());
                            params.getSseEmitter().send(SseEmitter.event().data(chatVO));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onComplete((Response<AiMessage> response) -> {
                    System.out.println(response);
                    params.getSseEmitter().complete();
                })
                .onError((Throwable error) -> error.printStackTrace())
                .start();
    }

    @Override
    public String genChart(String message, Long userId) {
        //  todo  后面再改
        AbstractLLMService llmService = LLMContext.getLLMServiceByName(LlmConstant.DEFAULT_MODEL);
        IChatAssistant assistant = AiServices.builder(IChatAssistant.class)
                .chatLanguageModel(llmService.buildChatLLM(new LLMBuilderProperties()))
                .build();
        String chat = assistant.genChart(message);
        // 同步调用
        log.info("智普 AI 返回的结果 {}", chat);
        return chat;
    }
    /**
     * 初始化所有模型
     */
    @Override
    public void init() {
        LambdaQueryWrapper<AiModel> queryWrapper = new LambdaQueryWrapper<>();
        List<AiModel> aiModels = aiModelRepository.list(queryWrapper);
        aiModelSettingService.init(aiModels);
    }
}





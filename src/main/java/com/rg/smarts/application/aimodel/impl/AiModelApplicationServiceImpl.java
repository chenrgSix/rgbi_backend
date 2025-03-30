package com.rg.smarts.application.aimodel.impl;

import cn.hutool.core.util.StrUtil;
import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.application.aimodel.DialoguesApplicationService;
import com.rg.smarts.application.knowledge.dto.DocumentKnn;
import com.rg.smarts.application.knowledge.service.KnowledgeBaseApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.helper.LLMContext;
import com.rg.smarts.domain.aimodel.model.AssistantChatParams;
import com.rg.smarts.domain.aimodel.model.SseAskParams;
import com.rg.smarts.domain.aimodel.service.AiModelDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import com.rg.smarts.interfaces.dto.ai.AiModelUpdateRequest;
import com.rg.smarts.interfaces.dto.ai.ChatRequest;
import com.rg.smarts.interfaces.vo.ai.AiModelVO;
import com.rg.smarts.interfaces.vo.ai.LLMModelVo;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Comparator;
import java.util.List;

/**
 * @author czr
 * @description 针对表【AiModel(AiModel表)】的数据库操作Service实现
 */
@Service
@Slf4j
public class AiModelApplicationServiceImpl implements AiModelApplicationService {
    @Resource
    private AiModelDomainService aiModelDomainService;

    @Resource
    private UserApplicationService userApplicationService;
    @Resource
    private DialoguesApplicationService dialoguesApplicationService;
    @Resource
    private KnowledgeBaseApplicationService knowledgeBaseApplicationService;
    @Override
    public String genChart(String message, Long userId) {
        return aiModelDomainService.genChart(message, userId);
    }

    @Override
    public AiModel addOne(AiModelAddRequest aiModelAddRequest) {
        AiModel aiModel = new AiModel();
        BeanUtils.copyProperties(aiModelAddRequest, aiModel);
        aiModelDomainService.addOne(aiModel);
        return aiModel;
    }

    @Override
    public Boolean updateAiModel(AiModelUpdateRequest aiModelUpdateRequest) {
        AiModel aiModel = new AiModel();
        BeanUtils.copyProperties(aiModelUpdateRequest,aiModel);
        return aiModelDomainService.updateAiModel(aiModel);
    }
    @Override
    public AiModelVO getAiModelById(Long aiModelId) {
        AiModel aiModel = aiModelDomainService.getAiModelByIdOrThrow(aiModelId);
        AiModelVO aiModelVO = new AiModelVO();
        BeanUtils.copyProperties(aiModel, aiModelVO);
        return aiModelVO;
    }
    @Override
    public List<LLMModelVo> getSupportLLMModel() {
        return LLMContext.getAllServices().values()
                .stream()
                .map(item -> {
                    AiModel aiModel = item.getAiModel();
                    LLMModelVo modelInfo = new LLMModelVo();
                    modelInfo.setModelId(aiModel.getId());
                    modelInfo.setModelName(aiModel.getName());
                    modelInfo.setModelPlatform(aiModel.getPlatform());
                    modelInfo.setIsEnable(aiModel.getIsEnable());
                    modelInfo.setIsFree(aiModel.getIsFree());
                    return modelInfo;
                })
                .sorted(Comparator.comparingInt(item -> (item.getIsFree() == 1 && item.getIsEnable() == 1) ? 0 : 1))
                .toList();
        }
    @Transactional
    @Override
    public void chatStream(ChatRequest chatRequest, SseEmitter sseEmitter, HttpServletRequest request) {
        SseAskParams sseAskParams = getSseAskParams(chatRequest, sseEmitter, request);
        Long kbId = chatRequest.getKbId();
        if (kbId==null){
            aiModelDomainService.commonChat(sseAskParams);
            return;
        }
        Long userId = userApplicationService.getLoginUser(request).getId();
        List<DocumentKnn> documentKnns = knowledgeBaseApplicationService.searchDocumentChunk(userId, kbId, chatRequest.getContent());
        sseAskParams.getAssistantChatParams().setSearchResult(documentKnns.toString());
        ragChatStream(kbId,userId,sseAskParams);
    }

    private void ragChatStream(Long kbId, Long userId,SseAskParams sseAskParams) {
        ContentRetriever contentRetriever = knowledgeBaseApplicationService.getContentRetriever(kbId,userId);
        aiModelDomainService.ragChat(sseAskParams,contentRetriever);
    }
    protected SseAskParams getSseAskParams(ChatRequest chatRequest, SseEmitter sseEmitter, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        SseAskParams sseAskParams = new SseAskParams();
        sseAskParams.setUserId(loginUser.getId());
        sseAskParams.setModelName(LlmConstant.DEFAULT_MODEL);
        if (StrUtil.isNotBlank(chatRequest.getModelName())){
            AiModel aiModel = LLMContext.getAllServices().get(chatRequest.getModelName()).getAiModel();
            if (!aiModel.getEnable()){
                throw new BusinessException(ErrorCode.B_LLM_SERVICE_DISABLED);
            }
            sseAskParams.setModelName(chatRequest.getModelName());
        }
        sseAskParams.setSseEmitter(sseEmitter);
        Long validMemoryId = dialoguesApplicationService.getMemoryIdOrAdd(
                chatRequest.getContent(),
                loginUser.getId(),
                chatRequest.getMemoryId());
        AssistantChatParams assistantChatParams = new AssistantChatParams();
        assistantChatParams.setMemoryId(validMemoryId);
        assistantChatParams.setContext(chatRequest.getContent());
        sseAskParams.setAssistantChatParams(assistantChatParams);
        return sseAskParams;
    }
    @Override
    public void init() {
        aiModelDomainService.init();
    }

}





package com.rg.smarts.application.aimodel.impl;

import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.model.AssistantChatParams;
import com.rg.smarts.domain.aimodel.model.SseAskParams;
import com.rg.smarts.domain.aimodel.service.AiModelDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.interfaces.dto.ai.AiModelAddRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    public void chatStream(Long memoryId, String content, SseEmitter sseEmitter, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Long validMemoryId = dialoguesApplicationService.getMemoryIdOrAdd(content, loginUser.getId(), memoryId);
        SseAskParams sseAskParams = new SseAskParams();
        sseAskParams.setUserId(loginUser.getId());
        // todo先默认使用GLM-4-Flash
        sseAskParams.setModelName(LlmConstant.DEFAULT_MODEL);
        sseAskParams.setSseEmitter(sseEmitter);
        AssistantChatParams assistantChatParams = new AssistantChatParams();
        assistantChatParams.setMemoryId(validMemoryId);
        assistantChatParams.setContext(content);
        sseAskParams.setAssistantChatParams(assistantChatParams);

        aiModelDomainService.commonChat(sseAskParams);
    }
    @Override
    public void init() {
        aiModelDomainService.init();
    }

}





package com.rg.smarts.application.dialogues.impl;

import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.service.DialoguesDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.interfaces.vo.dialogues.ChatVO;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description:
 */
@Service
public class DialoguesApplicationServiceImpl implements DialoguesApplicationService {
    @Autowired
    public DialoguesDomainService dialoguesDomainService;
    @Resource
    private UserApplicationService userApplicationService;

    @Override
    public ChatVO chat(Long memoryId, String content, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Long validMemoryId = dialoguesDomainService.getMemoryIdOrAdd(content, loginUser.getId(), memoryId);
        String chatResponse = dialoguesDomainService.chat(content, loginUser.getId(), validMemoryId);
        ChatVO chatVO = new ChatVO();
        chatVO.setChatResponse(chatResponse);
        chatVO.setId(validMemoryId);
        return chatVO;
    }

    @Override
    public void chatStream(Long memoryId, String content, SseEmitter sseEmitter, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Long validMemoryId = dialoguesDomainService.getMemoryIdOrAdd(content, loginUser.getId(), memoryId);
        TokenStream tokenStream = dialoguesDomainService.chatStream(content, loginUser.getId(), validMemoryId);
        tokenStream.onNext((String token) -> {
                    System.out.println(token);
                    try {
                        if (token != null) {
                            ChatVO chatVO = new ChatVO();
                            chatVO.setChatResponse(token);
                            chatVO.setId(validMemoryId);
                            sseEmitter.send(SseEmitter.event().data(chatVO));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onComplete((Response<AiMessage> response) -> {
                    System.out.println(response);
                    sseEmitter.complete();
                })
                .onError((Throwable error) -> error.printStackTrace())
                .start();
    }
    

    @Override
    public List<DialogueSummaryVO> getBatchOfChatList(HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        List<Dialogues> records = dialoguesDomainService.getBatchOfChatList(loginUser.getId());
        return records.stream().map(dialogues -> {
            DialogueSummaryVO dialogueSummaryVO = new DialogueSummaryVO();
            BeanUtils.copyProperties(dialogues, dialogueSummaryVO);
            return dialogueSummaryVO;
        }).toList();
    }

    @Override
    public DialoguesVO getDialogueById(Long memoryId, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Dialogues dialogues = dialoguesDomainService.getDialoguesById(memoryId);
        ThrowUtils.throwIf(!Objects.equals(dialogues.getUserId(), loginUser.getId()), ErrorCode.OPERATION_ERROR, "您并无该对话内容");
        DialoguesVO dialoguesVO = new DialoguesVO();
        BeanUtils.copyProperties(dialogues, dialoguesVO);
        return dialoguesVO;
    }
}

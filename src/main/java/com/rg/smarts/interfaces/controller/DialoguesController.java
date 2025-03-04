package com.rg.smarts.interfaces.controller;

import com.rg.smarts.application.dialogues.DialoguesApplicationService;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.chat.ChatRequest;
import com.rg.smarts.interfaces.vo.dialogues.ChatVO;
import com.rg.smarts.interfaces.vo.dialogues.DialogueSummaryVO;
import com.rg.smarts.interfaces.vo.dialogues.DialoguesVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


/**
 * @Author: czr
 * @CreateTime: 2025-03-01
 * @Description: ai聊天对话
 */
@Slf4j
@RestController
@RequestMapping("dialogues")
public class DialoguesController {
    @Resource
    private DialoguesApplicationService dialoguesApplicationService;

    @PostMapping("chat")
    public BaseResponse<ChatVO> chat(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        ChatVO chat = dialoguesApplicationService.chat(chatRequest.getMemoryId(), chatRequest.getContent(), request);
        return ResultUtils.success(chat);
    }

    @GetMapping("chat/list")
    public BaseResponse<List<DialogueSummaryVO>> getChatList(HttpServletRequest request) {
        List<DialogueSummaryVO> result = dialoguesApplicationService.getBatchOfChatList(request);
        return ResultUtils.success(result);
    }

    @GetMapping("chat/info")
    public BaseResponse<DialoguesVO> getDialogueById(Long memoryId, HttpServletRequest request) {
        DialoguesVO result = dialoguesApplicationService.getDialogueById(memoryId, request);
        return ResultUtils.success(result);
    }


    @PostMapping(value = "chat/steam")
    public SseEmitter chatStream(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        // langchain4j已经默认帮我们引入RxJava了，我们就不要自己引入了
        SseEmitter sseEmitter = new SseEmitter(0L);// 0L表示不设置超时时间
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion:{} 结束", chatRequest.getMemoryId());
        });
        dialoguesApplicationService.chatStream(chatRequest.getMemoryId(), chatRequest.getContent(), sseEmitter, request);
        return sseEmitter;
    }


}

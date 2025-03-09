package com.rg.smarts.interfaces.controller;

import com.rg.smarts.application.aimodel.AiModelApplicationService;
import com.rg.smarts.interfaces.dto.ai.ChatRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: czr
 * @CreateTime: 2025-03-09
 * @Description: ai模型相关功能
 */
@RestController
@RequestMapping("/aimodel")
@Slf4j
public class AiModelController {
    @Resource
    private AiModelApplicationService aiModelApplicationService;
    @PostMapping(value = "chat/steam")
    public SseEmitter chatStream(@RequestBody ChatRequest chatRequest, HttpServletRequest request) {
        // langchain4j已经默认帮我们引入RxJava了，我们就不要自己引入了
        SseEmitter sseEmitter = new SseEmitter(0L);// 0L表示不设置超时时间
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion:{} 结束", chatRequest.getMemoryId());
        });
        aiModelApplicationService.chatStream(chatRequest.getMemoryId(), chatRequest.getContent(), sseEmitter, request);
        return sseEmitter;
    }


}

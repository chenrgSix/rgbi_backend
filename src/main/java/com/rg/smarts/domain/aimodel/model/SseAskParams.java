package com.rg.smarts.domain.aimodel.model;

import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
public class SseAskParams {


    private Long userId;

    private String modelName;

    private SseEmitter sseEmitter;

    /**
     * 组装LLMService所属的属性，非必填
     */
    private LLMBuilderProperties llmBuilderProperties;

    /**
     * 最终提交给llm的信息，必填
     */
    private AssistantChatParams assistantChatParams;
}

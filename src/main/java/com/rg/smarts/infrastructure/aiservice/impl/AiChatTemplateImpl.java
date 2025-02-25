package com.rg.smarts.infrastructure.aiservice.impl;

import com.rg.smarts.infrastructure.aiservice.AiChatTemplate;
import com.rg.smarts.infrastructure.utils.BaseTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author: czr
 * @CreateTime: 2025-02-25
 * @Description:
 */
@Service
public class AiChatTemplateImpl {
    @Resource
    private ZhipuAiChatModel zhiPuAiChatModel;
    @Resource
    private BaseTools baseTools;
    @Bean
    AiChatTemplate aiChat(){
        return AiServices.builder(AiChatTemplate.class)
                .chatLanguageModel(zhiPuAiChatModel)
                .chatMemoryProvider(memoryId-> MessageWindowChatMemory.withMaxMessages(10))
                .tools(baseTools)
                .build();
    }
}

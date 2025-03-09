package com.rg.smarts.infrastructure.llm;


import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.model.LLMBuilderProperties;
import com.rg.smarts.domain.aimodel.model.OllamaSetting;
import com.rg.smarts.domain.aimodel.service.AbstractLLMService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.apache.commons.lang3.StringUtils;

import static com.rg.smarts.domain.aimodel.constant.LlmConstant.SysConfigKey.OLLAMA_SETTING;


public class OllamaLLMService extends AbstractLLMService<OllamaSetting> {

    public OllamaLLMService(AiModel aiModel) {
        super(aiModel, OLLAMA_SETTING, OllamaSetting.class);
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.isNotBlank(modelPlatformSetting.getBaseUrl()) && aiModel.getIsEnable();
    }

    @Override
    public ChatLanguageModel buildChatLLM(LLMBuilderProperties properties) {
        double temperature = 0.7;
        if (null != properties && properties.getTemperature() > 0 && properties.getTemperature() <= 1) {
            temperature = properties.getTemperature();
        }

        return OllamaChatModel.builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .modelName(aiModel.getName())
                .temperature(temperature)
                .build();
    }

    @Override
    public StreamingChatLanguageModel buildStreamingChatLLM(LLMBuilderProperties properties) {
        double temperature = 0.7;
        if (null != properties && properties.getTemperature() > 0 && properties.getTemperature() <= 1) {
            temperature = properties.getTemperature();
        }
        return OllamaStreamingChatModel.builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .modelName(aiModel.getName())
                .temperature(temperature)
                .build();
    }


}

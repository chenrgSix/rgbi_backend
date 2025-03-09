package com.rg.smarts.infrastructure.llm;

import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.model.LLMBuilderProperties;
import com.rg.smarts.domain.aimodel.model.ZhiPuAiSetting;
import com.rg.smarts.domain.aimodel.service.AbstractLLMService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;


/**
 * ZhiPu LLM service
 */
@Slf4j
@Accessors(chain = true)
public class ZhiPuLLMService extends AbstractLLMService<ZhiPuAiSetting> {
    public ZhiPuLLMService(AiModel model) {
        super(model, LlmConstant.SysConfigKey.ZHHIPU_SETTING, ZhiPuAiSetting.class);
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.isNotBlank(modelPlatformSetting.getSecretKey()) && aiModel.getEnable();
    }

    @Override
    public ChatLanguageModel buildChatLLM(LLMBuilderProperties properties) {
        if (StringUtils.isBlank(modelPlatformSetting.getSecretKey())) {
            throw new BusinessException(ErrorCode.B_LLM_SECRET_KEY_NOT_SET);
        }
        double temperature = 0.7;
        if (null != properties && properties.getTemperature() > 0 && properties.getTemperature() <= 1) {
            temperature = properties.getTemperature();
        }
        ZhipuAiChatModel.ZhipuAiChatModelBuilder builder = ZhipuAiChatModel.builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .model(aiModel.getName())
                .temperature(temperature)
                .apiKey(modelPlatformSetting.getSecretKey())
                .callTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60));
        if (StringUtils.isNotBlank(modelPlatformSetting.getBaseUrl())) {
            builder.baseUrl(modelPlatformSetting.getBaseUrl());
        }
        return builder.build();
    }

    @Override
    public StreamingChatLanguageModel buildStreamingChatLLM(LLMBuilderProperties properties) {
        if (StringUtils.isBlank(modelPlatformSetting.getSecretKey())) {
            throw new BusinessException(ErrorCode.B_LLM_SECRET_KEY_NOT_SET);
        }
        double temperature = 0.7;
        if (null != properties && properties.getTemperature() > 0 && properties.getTemperature() <= 1) {
            temperature = properties.getTemperature();
        }
        ZhipuAiStreamingChatModel.ZhipuAiStreamingChatModelBuilder builder = ZhipuAiStreamingChatModel
                .builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .model(aiModel.getName())
                .temperature(temperature)
                .apiKey(modelPlatformSetting.getSecretKey())
                .callTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60));

        return builder.build();
    }
}

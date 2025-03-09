package com.rg.smarts.infrastructure.llm;

import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.model.LLMBuilderProperties;
import com.rg.smarts.domain.aimodel.model.OpenAiSetting;
import com.rg.smarts.domain.aimodel.service.AbstractLLMService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * OpenAi LLM service
 */
@Slf4j
@Accessors(chain = true)
public class OpenAiLLMService extends AbstractLLMService<OpenAiSetting> {
    public OpenAiLLMService(AiModel model) {
        super(model, LlmConstant.SysConfigKey.OPENAI_SETTING, OpenAiSetting.class);
    }

    /**
     * 兼容OpenAi的模型，重新指定系统配置项，并使用本构造器进行初始化
     *
     * @param model        adi_ai_model中的模型
     * @param sysConfigKey 系统配置项名称，如DeepSeek兼容openai的api格式，DeepSeek的系统配置项在adi_sys_config中为deepseek_setting
     */
    public OpenAiLLMService(AiModel model, String sysConfigKey) {
        super(model, sysConfigKey, OpenAiSetting.class);
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
        OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .modelName(aiModel.getName())
                .temperature(temperature)
                .apiKey(modelPlatformSetting.getSecretKey());
        if (StringUtils.isNotBlank(modelPlatformSetting.getBaseUrl())) {
            builder.baseUrl(modelPlatformSetting.getBaseUrl());
        }
        if (null != proxy) {
            builder.proxy(proxy);
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
        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel
                .builder()
                .baseUrl(modelPlatformSetting.getBaseUrl())
                .modelName(aiModel.getName())
                .temperature(temperature)
                .apiKey(modelPlatformSetting.getSecretKey())
                .timeout(Duration.of(60, ChronoUnit.SECONDS));
        if (null != proxy) {
            builder.proxy(proxy);
        }
        return builder.build();
    }
}

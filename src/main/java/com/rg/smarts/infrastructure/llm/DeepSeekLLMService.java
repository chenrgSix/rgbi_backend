package com.rg.smarts.infrastructure.llm;

import com.rg.smarts.domain.aimodel.constant.LlmConstant;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * DeepSeek API格式兼容OpenAi
 */
@Slf4j
@Accessors(chain = true)
public class DeepSeekLLMService extends OpenAiLLMService {
    public DeepSeekLLMService(AiModel model) {
        super(model, LlmConstant.SysConfigKey.DEEPSEEK_SETTING);
    }
}

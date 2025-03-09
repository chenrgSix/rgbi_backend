package com.rg.smarts.domain.aimodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class OpenAiSetting extends CommonAiPlatformSetting{
    /**
     * 默认为空，使用代码中对应模型的地址
     */
    @JsonProperty("secret_key")
    private String secretKey;
}

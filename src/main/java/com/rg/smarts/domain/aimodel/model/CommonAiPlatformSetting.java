package com.rg.smarts.domain.aimodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommonAiPlatformSetting {
    
    /**
     * 默认为空，使用代码中对应模型的地址
     */
    @JsonProperty("base_url")
    private String baseUrl;
}

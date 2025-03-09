package com.rg.smarts.interfaces.dto.ai;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 模型添加请求
 */
@Data
public class AiModelAddRequest implements Serializable {
    /**
     * 模型名称
     */
    @Schema(title = "模型名称")
    private String name;

    /**
     * 类别
     */
    @Schema(title = "模型类型:text,image,embedding,multimodal")
    private String type;

    /**
     * 配置
     */
    @Schema(title = "配置")
    private String setting;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 平台
     */
    @Schema(title = "平台：Ollama、OpenAI、Claude...")
    private String platform;

    /**
     * 最大输入token
     */
    @Schema(title = "最大输入token")
    private Integer maxInputTokens;

    /**
     * 最大输出token
     */
    @Schema(title = "最大输出token")
    private Integer maxOutputTokens;

    /**
     * 是否免费
     */
    @Schema(title = "是否免费")
    private Integer isFree;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    private Integer isEnable;

    private static final long serialVersionUID = 1L;
}
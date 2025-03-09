package com.rg.smarts.interfaces.vo.ai;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型表
 * @TableName ai_model
 */
@Data
public class LLMModelVo implements Serializable {
    /**
     * 模型id
     */
    private Long modelId;


    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 平台
     */
    private String modelPlatform;


    /**
     * 是否免费
     */
    private Integer isFree;

    /**
     * 是否启用
     */
    private Integer isEnable;


    private static final long serialVersionUID = 1L;
}
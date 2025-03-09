package com.rg.smarts.interfaces.vo.ai;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AiModelVO implements Serializable {
    /**
     * id
     */

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 类别
     */
    private String type;

    /**
     * 配置
     */
    private String setting;

    /**
     * 备注
     */
    private String remark;

    /**
     * 平台
     */
    private String platform;

    /**
     * 最大输入token
     */
    private Integer maxInputTokens;

    /**
     * 最大输出token
     */
    private Integer maxOutputTokens;

    /**
     * 是否免费
     */
    private Integer isFree;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;
}
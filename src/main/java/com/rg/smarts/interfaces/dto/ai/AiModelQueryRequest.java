package com.rg.smarts.interfaces.dto.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AiModelQueryRequest extends PageRequest implements Serializable {
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
     * 平台
     */
    private String platform;

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
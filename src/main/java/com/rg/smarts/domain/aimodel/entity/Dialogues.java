package com.rg.smarts.domain.aimodel.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 对话表
 * @TableName dialogues
 */
@TableName(value ="dialogues")
@Data
public class Dialogues implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private String chatContent;

    /**
     * 
     */
    private String chatTitle;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.rg.smarts.domain.aimodel.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.rg.smarts.infrastructure.mapper.handler.JsonTypeHandler;
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
    @TableField("userId")
    private Long userId;

    /**
     * 
     */
    @TableField("chatContent")
    private String chatContent;

    /**
     * 
     */
    @TableField("chatTitle")
    private String chatTitle;



    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    @TableField(value = "kbIds", typeHandler = JsonTypeHandler.class)
    private List<Long> kbIds;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
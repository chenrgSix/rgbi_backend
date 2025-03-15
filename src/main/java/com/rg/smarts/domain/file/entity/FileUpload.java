package com.rg.smarts.domain.file.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件表
 * @TableName file_upload
 */
@TableName(value ="file_upload")
@Data
public class FileUpload implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 文件名，实际存储此文件的名称，带后缀
     */
    private String fileName;

    /**
     * 原始名称，不带后缀
     */
    private String displayName;

    /**
     * 文件的类型（后缀）
     */
    private String fileSuffix;

    /**
     * 
     */
    private Long fileSize;

    /**
     * 文件的路径
     */
    private String path;

    /**
     * 
     */
    private String fileDesc;

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
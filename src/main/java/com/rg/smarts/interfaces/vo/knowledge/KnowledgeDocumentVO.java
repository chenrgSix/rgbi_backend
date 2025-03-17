package com.rg.smarts.interfaces.vo.knowledge;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识文档
 */
@Data
public class KnowledgeDocumentVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文档类型
     */
    private String docType;

    /**
     * 文档状态 -1：解析失败 0：未解析 1：解析中 2：解析完成
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.rg.smarts.interfaces.vo.knowledge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 知识库
 * @TableName knowledge_base
 */

@Data
public class KnowledgeBaseVO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * id
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否公开，0：否；1：是
     */
    private Integer isPublic;

    /**
     * 文档数量
     */
    private Integer docNum;

    /**
     * 最大分割大小
     */
    private Integer ingestMaxSegment;

    /**
     * 最大重叠大小
     */
    private Integer ingestMaxOverlap;

    /**
     * 模型名称
     */
    private String ingestModelName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
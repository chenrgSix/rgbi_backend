package com.rg.smarts.interfaces.dto.knowledge;

import lombok.Data;

import java.io.Serializable;

/**
 * 知识库创建
 */
@Data
public class KnowledgeBaseAddRequest implements Serializable {

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
     * 最大分割大小
     */
    private Integer ingestMaxSegment;

    /**
     * 最大重叠大小
     */
    private Integer ingestMaxOverlap;

    /**
     * todo 模型名称
     */
//    private String ingestModelName;

    private static final long serialVersionUID = 1L;
}
package com.rg.smarts.interfaces.dto.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 知识文档
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class KnowledgeDocumentQueryRequest extends PageRequest implements Serializable {
    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 文档状态 -1：解析失败 0：未解析 1：解析中 2：解析完成
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
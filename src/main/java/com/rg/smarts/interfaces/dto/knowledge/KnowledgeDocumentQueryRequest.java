package com.rg.smarts.interfaces.dto.knowledge;

import com.rg.smarts.infrastructure.common.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 知识文档
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeDocumentQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;
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
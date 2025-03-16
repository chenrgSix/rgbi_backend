package com.rg.smarts.interfaces.dto.knowledge;

import lombok.Data;

import java.io.Serializable;

/**
 * 知识文档添加
 */
@Data
public class KnowledgeAddDocumentRequest implements Serializable {
    /**
     * 知识库ID
     */
    private Long kbId;

    private static final long serialVersionUID = 1L;
}
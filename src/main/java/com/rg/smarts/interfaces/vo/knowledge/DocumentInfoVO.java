package com.rg.smarts.interfaces.vo.knowledge;

import com.rg.smarts.application.knowledge.dto.DocumentChunk;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 知识文档
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentInfoVO extends KnowledgeDocumentVO implements Serializable{
    private List<DocumentChunk> chunks;
    /**
     * 文档名称
     */
    private String displayName;

    /**
     * 文档大小
     */
    private Long fileSize;
    /**
     * 总页数
     */
    private int pages ;
    private int size ;
    private int current ;

    private static final long serialVersionUID = 1L;
}
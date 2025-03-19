package com.rg.smarts.domain.knowledge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.knowledge.dto.DocumentInfoDTO;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;
import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import dev.langchain4j.data.document.Document;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description: 知识库操作的domain层
 */
public interface KnowledgeBaseDomainService {
    Boolean addKnowledgeDocument(Long kbId, Long userId, Long fileId, String docType, Integer docNum);

    KnowledgeDocument getKnowledgeDocumentById(Long documentId);

    //    解析文档
    void loadDocument(KnowledgeDocument knowledgeDocument, String filePath);

    Boolean verifyIdentity(Long kb_id, Long userId);

    Boolean addKnowledgeBase(KnowledgeBase knowledgeBase);

    Page<KnowledgeBase> getKnowledgeBasePage(Page<KnowledgeBase> knowledgeBasePage, QueryWrapper<KnowledgeBase> queryWrapper);

    KnowledgeBase getKnowledgeBaseById(Long id);


    Page<KnowledgeDocument> getKnowledgeDocPage(Page<KnowledgeDocument> knowledgeDocumentPage, QueryWrapper<KnowledgeDocument> queryKnowledgeDocWrapper);

    DocumentInfoDTO getDocumentInfo(KnowledgeDocument document,int current,int pageSize);

}

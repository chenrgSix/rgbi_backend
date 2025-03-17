package com.rg.smarts.domain.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.knowledge.constant.EmbeddingConstant;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;
import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import com.rg.smarts.domain.knowledge.repository.KnowledgeBaseRepository;
import com.rg.smarts.domain.knowledge.repository.KnowledgeDocumentRepository;
import com.rg.smarts.domain.knowledge.service.EmbeddingService;
import com.rg.smarts.domain.knowledge.service.KnowledgeBaseDomainService;
import com.rg.smarts.domain.knowledge.valueobject.DocumentStatusEnum;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description:
 */
@Service
public class KnowledgeBaseDomainServiceImpl implements KnowledgeBaseDomainService {
    @Resource
    private KnowledgeBaseRepository knowledgeBaseRepository;

    @Resource
    private KnowledgeDocumentRepository knowledgeDocumentRepository;

    @Resource
    private EmbeddingService embeddingService;

    @Override
    public Boolean addKnowledgeDocument(Long kbId, Long userId, Long fileId, String docType,Integer docNum){
        KnowledgeDocument knowledgeDocument = new KnowledgeDocument();
        knowledgeDocument.setKbId(kbId);
        knowledgeDocument.setUserId(userId);
        knowledgeDocument.setFileId(fileId);
        knowledgeDocument.setDocType(docType);
        boolean save = knowledgeDocumentRepository.save(knowledgeDocument);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(kbId);
        docNum=docNum+1;
        knowledgeBase.setDocNum(docNum);
        return knowledgeBaseRepository.updateById(knowledgeBase);
    }
    @Override
    public KnowledgeDocument getKnowledgeDocumentById(Long documentId){
        return knowledgeDocumentRepository.getById(documentId);
    }

    //    解析文档
    @Override
    @Transactional
    @Async
    public void loadDocument(KnowledgeDocument knowledgeDocument, String filePath) {
        String docType = knowledgeDocument.getDocType();
        Long id = knowledgeDocument.getId();
        Long kbId = knowledgeDocument.getKbId();
        Document result = null;
        KnowledgeDocument updateDoc = new KnowledgeDocument();;
        try{
            if (docType.equalsIgnoreCase("txt")) {
                result = UrlDocumentLoader.load(filePath, new TextDocumentParser());
            } else if (docType.equalsIgnoreCase("pdf")) {
                result = UrlDocumentLoader.load(filePath, new ApachePdfBoxDocumentParser());
            } else if (EmbeddingConstant.DOC_TYPES.contains(docType)) {
                result = UrlDocumentLoader.load(filePath, new ApachePoiDocumentParser());
            }
            ThrowUtils.throwIf(result == null, ErrorCode.OPERATION_ERROR,"文档内容为空或文档格式不支持");
            result.metadata().put(EmbeddingConstant.DOC_ID,id);
            result.metadata().put(EmbeddingConstant.KB_ID,kbId);
            // Metadata { metadata = {doc_id=66666, kb_id=66666, url=http://127.0.0.1:9000/document/1890713823228264450/0ws3dQvtRP.docx} }
            updateDoc.setId(id);
            updateDoc.setStatus(DocumentStatusEnum.RUN.getValue());
            knowledgeDocumentRepository.updateById(updateDoc);
            embeddingService.ingest(result, EmbeddingConstant.DEFAULT_INGEST_OVERLAP);
            updateDoc.setStatus(DocumentStatusEnum.COMPLETE.getValue());
            knowledgeDocumentRepository.updateById(updateDoc);
        }catch (Exception e) {
            updateDoc.setId(id);
            updateDoc.setStatus(DocumentStatusEnum.FAIL.getValue());
            knowledgeDocumentRepository.updateById(updateDoc);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }
    }
    @Override
    public Boolean verifyIdentity(Long kb_id, Long userId){
        KnowledgeBase knowledgeBaseById = this.getKnowledgeBaseById(kb_id);
        ThrowUtils.throwIf(knowledgeBaseById==null,ErrorCode.NOT_FOUND_ERROR,"知识库不存在");
        return knowledgeBaseById.isVisible(userId);
    }
    @Override
    public Boolean addKnowledgeBase(KnowledgeBase knowledgeBase) {
        return knowledgeBaseRepository.save(knowledgeBase);
    }

    @Override
    public Page<KnowledgeBase> getKnowledgeBasePage(Page<KnowledgeBase> knowledgeBasePage, QueryWrapper<KnowledgeBase> queryWrapper) {
        return knowledgeBaseRepository.page(knowledgeBasePage, queryWrapper);

    }

    @Override
    public KnowledgeBase getKnowledgeBaseById(Long id) {
        return knowledgeBaseRepository.getById(id);
    }

    @Override
    public Page<KnowledgeDocument> getKnowledgeDocPage(Page<KnowledgeDocument> knowledgeDocumentPage, QueryWrapper<KnowledgeDocument> queryKnowledgeDocWrapper) {
        return knowledgeDocumentRepository.page(knowledgeDocumentPage, queryKnowledgeDocWrapper);
    }


}

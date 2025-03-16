package com.rg.smarts.domain.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;
import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import com.rg.smarts.domain.knowledge.repository.KnowledgeBaseRepository;
import com.rg.smarts.domain.knowledge.repository.KnowledgeDocumentRepository;
import com.rg.smarts.domain.knowledge.service.EmbeddingService;
import com.rg.smarts.domain.knowledge.service.KnowledgeBaseDomainService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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

}

package com.rg.smarts.domain.knowledge.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description: 知识库操作的domain层
 */
public interface KnowledgeBaseDomainService {
    Boolean addKnowledgeDocument(Long kbId, Long userId, Long fileId, String docType, Integer docNum);

    Boolean addKnowledgeBase(KnowledgeBase knowledgeBase);

    Page<KnowledgeBase> getKnowledgeBasePage(Page<KnowledgeBase> knowledgeBasePage, QueryWrapper<KnowledgeBase> queryWrapper);

    KnowledgeBase getKnowledgeBaseById(Long id);


}

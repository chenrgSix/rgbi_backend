package com.rg.smarts.infrastructure.repository.knowledge;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import com.rg.smarts.domain.knowledge.repository.KnowledgeDocumentRepository;
import com.rg.smarts.infrastructure.mapper.KnowledgeDocumentMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
* @author 16152
* @description 针对表【knowledge_document(知识文档)】的数据库操作Service实现
* @createDate 2025-03-16 18:38:02
*/
@Repository
public class KnowledgeDocumentRepositoryImpl extends ServiceImpl<KnowledgeDocumentMapper, KnowledgeDocument>
    implements KnowledgeDocumentRepository {


}





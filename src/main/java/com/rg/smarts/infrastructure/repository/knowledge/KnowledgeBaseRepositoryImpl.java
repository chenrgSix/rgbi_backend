package com.rg.smarts.infrastructure.repository.knowledge;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;
import com.rg.smarts.domain.knowledge.repository.KnowledgeBaseRepository;
import com.rg.smarts.infrastructure.mapper.KnowledgeBaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author 16152
* @description 针对表【knowledge_base(知识库)】的数据库操作Service实现
* @createDate 2025-03-16 18:37:58
*/
@Repository
public class KnowledgeBaseRepositoryImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBase>
    implements KnowledgeBaseRepository {

}





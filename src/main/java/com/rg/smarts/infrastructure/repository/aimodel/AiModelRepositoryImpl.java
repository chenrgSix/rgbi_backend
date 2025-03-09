package com.rg.smarts.infrastructure.repository.aimodel;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.repository.AiModelRepository;
import com.rg.smarts.infrastructure.mapper.AiModelMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AiModelRepositoryImpl extends ServiceImpl<AiModelMapper, AiModel> implements AiModelRepository {

}

package com.rg.smarts.domain.aimodel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.aimodel.entity.AiModel;
import com.rg.smarts.domain.aimodel.model.SseAskParams;
import dev.langchain4j.rag.content.retriever.ContentRetriever;

/**
* @author czr
* @description 针对表【AiModel(Ai模型表)】的数据库操作Service
*/
public interface AiModelDomainService {


    AiModel addOne(AiModel aiModel);

    AiModel getAiModelByIdOrThrow(Long id);

    Boolean updateAiModel(AiModel aiModel);

    Page<AiModel> listAiModelByPage(Page<AiModel> aiModelPage, QueryWrapper<AiModel> queryWrapper);

    void commonChat(SseAskParams params);

    void ragChat(SseAskParams params);



    String genChart(String message, Long userId);

    void init();
}

package com.rg.smarts.domain.aimodel.service;

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

    void commonChat(SseAskParams params);

    void ragChat(SseAskParams params, ContentRetriever contentRetriever);



    String genChart(String message, Long userId);

    void init();
}

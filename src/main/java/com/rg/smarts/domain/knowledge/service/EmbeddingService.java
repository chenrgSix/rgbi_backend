package com.rg.smarts.domain.knowledge.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.store.embedding.filter.Filter;

/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 定义向量服务的接口类
 */
public interface EmbeddingService {

    ContentRetriever getContentRetriever(Long kbId);

    void ingest(Document document, int overlap);

    Embedding getVectorBySearch(String search);
}

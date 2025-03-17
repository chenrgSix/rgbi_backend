package com.rg.smarts.domain.knowledge.service;

import dev.langchain4j.data.document.Document;

/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 定义向量服务的接口类
 */
public interface EmbeddingService {

    void ingest(Document document, int overlap);
}

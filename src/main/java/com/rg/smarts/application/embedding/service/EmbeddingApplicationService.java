package com.rg.smarts.application.embedding.service;

/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 定义向量服务的接口类
 */
public interface EmbeddingApplicationService {
    void loadDocument(String filePath,String fileSuffix);
}

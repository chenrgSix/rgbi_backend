package com.rg.smarts.infrastructure.config;

import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import jakarta.annotation.Resource;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Author: czr
 * @CreateTime: 2025-03-13
 * @Description: 初始化向量存储
 */
@Configuration
public class EmbeddingStoreConfig {
//    ElasticsearchEmbeddingStore
    @Resource
    private RestClient client;
    @Bean
    public ElasticsearchEmbeddingStore elasticsearchEmbeddingStore() {
        return ElasticsearchEmbeddingStore.builder()
                .restClient(client)
//                .indexName("rg") //指定索引名称
                .build();
    }
}

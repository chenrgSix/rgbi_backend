package com.rg.smarts.infrastructure.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
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
public class EmbeddingConfig {
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

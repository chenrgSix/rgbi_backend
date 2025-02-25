package com.rg.smarts.infrastructure.config;


import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiEmbeddingModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


// 配置类，用于定义Spring容器中的Bean
@Configuration
@ConfigurationProperties(prefix = "langchain4j.zhipu")
@Data
public class ZhiPuConfig {
    private String apiKey;
    private String model;
    private Boolean logRequests;
    private Boolean logResponses;

    @Bean
    ZhipuAiChatModel zhiPuAiChatModel() {
        return ZhipuAiChatModel.builder()
                .apiKey(apiKey)
                .model(model)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .callTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    ZhipuAiEmbeddingModel zhipuAiEmbeddingModel() {
        return ZhipuAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .model(model)
                .callTimeout(Duration.ofSeconds(60))
                .connectTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .build();
    }



}

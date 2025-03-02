package com.rg.smarts.infrastructure.config;


import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.Duration;


// 配置类，用于定义Spring容器中的Bean
@Configuration
@ConfigurationProperties(prefix = "langchain4j.zhipu")
@Data
public class ZhiPuConfig {
    private String apiKey;
    private String chatModel;
    private Boolean logRequests;
    private Boolean logResponses;
    private Duration callTimeout = Duration.ofSeconds(60); // 默认值，可被配置文件覆盖
    private Duration connectTimeout = Duration.ofSeconds(60);
    private Duration writeTimeout = Duration.ofSeconds(60);
    private Duration readTimeout = Duration.ofSeconds(60);

    @Bean
    @Lazy
    ZhipuAiChatModel zhiPuAiChatModel() {
        return ZhipuAiChatModel.builder()
                .apiKey(apiKey)
                .model(chatModel)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .callTimeout(callTimeout)
                .connectTimeout(connectTimeout)
                .writeTimeout(writeTimeout)
                .readTimeout(readTimeout)
                .build();
    }
    @Bean
    @Lazy
    ZhipuAiStreamingChatModel zhipuAiStreamingChatModel() {
        return ZhipuAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .model(chatModel)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .callTimeout(callTimeout)
                .connectTimeout(connectTimeout)
                .writeTimeout(writeTimeout)
                .readTimeout(readTimeout)
                .build();
    }
}

package com.rg.intelligent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;


@Configuration
public class AppConfiguration {

    @Bean
    public RestClient.Builder restClientBuilder() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(120000); // 设置读取超时时间为2分钟
        return RestClient.builder().requestFactory(requestFactory);
    }

    // 其他Bean配置
}

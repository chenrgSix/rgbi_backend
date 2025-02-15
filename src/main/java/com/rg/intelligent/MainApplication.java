package com.rg.intelligent;

import com.rg.intelligent.mq.BiInitRabbitMQ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 */
// todo 如需开启 Redis，须移除 exclude 中的内容

@SpringBootApplication()//exclude = {RedisAutoConfiguration.class}
@MapperScan("com.rg.intelligent.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        BiInitRabbitMQ.doInit();
        SpringApplication.run(MainApplication.class, args);
    }

}

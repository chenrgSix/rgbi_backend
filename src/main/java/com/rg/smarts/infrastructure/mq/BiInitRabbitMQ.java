package com.rg.smarts.infrastructure.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rg.smarts.domain.chart.constant.BiMqConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建测试程序用到的交换机和队列
 */
@Slf4j
@Component
public class BiInitRabbitMQ {
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;


    @PostConstruct
    public void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setPort(port);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 声明死信队列
            channel.exchangeDeclare(BiMqConstant.BI_DLX_EXCHANGE_NAME, "direct");
            channel.queueDeclare(BiMqConstant.BI_DLX_QUEUE_NAME, true, false, false, null);
            channel.queueBind(BiMqConstant.BI_DLX_QUEUE_NAME, BiMqConstant.BI_DLX_EXCHANGE_NAME, BiMqConstant.BI_DLX_ROUTING_KEY);

            channel.exchangeDeclare(BiMqConstant.BI_EXCHANGE_NAME, "direct");

            Map<String, Object> arg = new HashMap<String, Object>();
            arg.put("x-dead-letter-exchange", BiMqConstant.BI_DLX_EXCHANGE_NAME);
            arg.put("x-dead-letter-routing-key", BiMqConstant.BI_DLX_ROUTING_KEY);
            //设置队列的死信交换机和死信路由键
            channel.queueDeclare(BiMqConstant.BI_QUEUE_NAME, true, false, false, arg);
            channel.queueBind(BiMqConstant.BI_QUEUE_NAME, BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
            log.info("消息队列启动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

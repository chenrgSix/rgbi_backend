package com.rg.intelligent.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rg.intelligent.constant.BiMqConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于创建测试程序用到的交换机和队列
 */
@Slf4j
public class BiInitRabbitMQ {

    public static void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
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
            channel.queueDeclare(BiMqConstant.BI_QUEUE_NAME, true, false, false, arg);
            channel.queueBind(BiMqConstant.BI_QUEUE_NAME, BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);
            log.info("消息队列启动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public static void main(String[] args) {
    //    try {
    //        ConnectionFactory factory = new ConnectionFactory();
    //        factory.setHost("localhost");
    //        //factory.setHost("my_rabbit");
    //        Connection connection = factory.newConnection();
    //        Channel channel = connection.createChannel();
    //        String EXCHANGE_NAME =  BiMqConstant.BI_EXCHANGE_NAME;
    //        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    //
    //        // 创建队列，随机分配一个队列名称
    //        String queueName = BiMqConstant.BI_QUEUE_NAME;
    //        channel.queueDeclare(queueName, true, false, false, null);
    //        channel.queueBind(queueName, EXCHANGE_NAME,  BiMqConstant.BI_ROUTING_KEY);
    //    } catch (Exception e) {
    //
    //    }

    //}
}

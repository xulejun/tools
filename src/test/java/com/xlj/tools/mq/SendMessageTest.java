package com.xlj.tools.mq;

import cn.hutool.core.lang.UUID;
import com.xlj.tools.ToolsApplication;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

/**
 * RabbitMq测试
 *
 * @author xlj
 * @date 2021/3/17
 */
@SpringBootTest(classes = ToolsApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class SendMessageTest {
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String exchange = "xlj-exchange";
    private String routingKey = "xlj.routingKey";
//    private String routingKey = "xlj.*";
    private String queue = "xlj-queue";

    /**
     * RabbitTemplate发消息
     */
    @Test
    public void sendMessage() {
        // 发消息，简单消息
        String message = "hello,rabbitMq";
//        rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(UUID.randomUUID().toString()));
//        log.info(MessageFormat.format("发送的消息:{0}", message));

        // 如果发送的是个对象，我们会使用序列化机制，将对象给写出去。所以对象必须实现Serializable接口。还可转化为Json格式。
//        for (int i = 0; i < 10; i++) {
        User user = User.builder().id(1).name("xlj").build();

        rabbitTemplate.convertAndSend(exchange, routingKey, user, new CorrelationData(UUID.randomUUID().toString()));
        log.info(MessageFormat.format("发送的消息:{0}", user));
//        }
    }

    @Test
    public void create() {
        // 创建一个交换机，默认为直连交换机
        amqpAdmin.declareExchange(new DirectExchange(exchange));
        // 创建队列
        amqpAdmin.declareQueue(new Queue(queue));
        // 创建绑定关系
        Binding binding = new Binding(queue, DestinationType.QUEUE, exchange, routingKey, null);
        amqpAdmin.declareBinding(binding);
        log.info("交换机：{}，队列：{}，路由键：{}，绑定成功", exchange, queue, routingKey);
    }

    @Test
    public void delete() {
        amqpAdmin.deleteExchange(exchange);
        amqpAdmin.deleteQueue(queue);
        log.info("交换机：{}，队列：{}，已删除", exchange, queue);
    }
}

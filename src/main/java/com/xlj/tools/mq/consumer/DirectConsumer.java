package com.xlj.tools.mq.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xlj.tools.mq.consumer.BootQueueName.*;

/**
 * 路由-直连模式，通过routingKey消费消息
 *
 * @author xlj
 * @date 2020/12/1 21:36
 */
@Component
public class DirectConsumer {
    @RabbitListener(bindings = {@QueueBinding(value = @Queue, // 默认创建临时队列
                    exchange = @Exchange(value = BOOT_DIRECT_EXCHANGE_NAME, type = "direct"),
                    key = {BOOT_INFO_ROUTING_KEY, BOOT_ERROR_ROUTING_KEY})})
    public void receiveMessage1(String message) {
        System.out.println("监听队列：" + BOOT_DIRECT_EXCHANGE_NAME + "\t消费者1消费消息:" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue, // 默认创建临时队列
                    exchange = @Exchange(value = BOOT_DIRECT_EXCHANGE_NAME, type = "direct"),
                    key = {BOOT_INFO_ROUTING_KEY})})
    public void receiveMessage2(String message) {
        System.out.println("监听队列：" + BOOT_DIRECT_EXCHANGE_NAME + "\t消费者2消费消息:" + message);
    }
}

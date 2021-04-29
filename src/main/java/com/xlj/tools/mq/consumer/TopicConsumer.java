package com.xlj.tools.mq.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xlj.tools.mq.consumer.BootQueueName.BOOT_TOPIC_EXCHANGE_NAME;

/**
 * 路由-动态路由模式，通过*、#动态匹配一个或者多个RoutingKey
 *
 * @author xlj
 * @date 2020/12/1 22:58
 */
@Component
public class TopicConsumer {
    @RabbitListener(bindings = {@QueueBinding(value = @Queue, // 默认创建临时队列
                    exchange = @Exchange(value = BOOT_TOPIC_EXCHANGE_NAME, type = "topic"),
                    key = {"user.*"})})
    public void  receiveMessage1(String message){
        System.out.println("监听队列：" + BOOT_TOPIC_EXCHANGE_NAME + "\t消费者1消费消息:" + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue, // 默认创建临时队列
                    exchange = @Exchange(value = BOOT_TOPIC_EXCHANGE_NAME, type = "topic"),
                    key = {"user.*","order.#"}
            )
    })
    public void  receiveMessage2(String message){
        System.out.println("监听队列：" + BOOT_TOPIC_EXCHANGE_NAME + "\t消费者2消费消息:" + message);
    }
}

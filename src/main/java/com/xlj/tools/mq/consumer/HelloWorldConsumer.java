package com.xlj.tools.mq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xlj.tools.mq.consumer.BootQueueName.BOOT_SIMPLE_QUEUE_NAME;


/**
 * springboot-HelloWorld模型-消费者
 *
 * @author xlj
 * @date 2020/11/29 17:56
 */
@Component
/**
 * 1.监听的队列名     2.持久化   3.自动删除
 */
@RabbitListener(queuesToDeclare = @Queue(value = BOOT_SIMPLE_QUEUE_NAME, durable = "true", autoDelete = "true"))
public class HelloWorldConsumer {
    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("监听队列：" + BOOT_SIMPLE_QUEUE_NAME + "\t消息:" + message);
    }
}

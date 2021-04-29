package com.xlj.tools.mq.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xlj.tools.mq.consumer.BootQueueName.BOOT_FANOUT_EXCHANGE_NAME;


/**
 * springboot-广播模型-消费者：订阅即可消费
 *
 * @author xlj
 * @date 2020/12/1 21:22
 */
@Component
public class FanoutConsumer {

    @RabbitListener(bindings = {@QueueBinding(value = @Queue, // 默认则创建临时队列
                    exchange = @Exchange(value = BOOT_FANOUT_EXCHANGE_NAME, type = "fanout")) // 绑定的交换机
                    })
    public void receiveMessage1(String message) {
        System.out.println("监听队列：" + BOOT_FANOUT_EXCHANGE_NAME + "\t消费者1消费消息:" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue, // 默认则创建临时队列
                    exchange = @Exchange(value = BOOT_FANOUT_EXCHANGE_NAME, type = "fanout")) // 绑定的交换机
                    })
    public void receiveMessage2(String message) {
        System.out.println("监听队列：" + BOOT_FANOUT_EXCHANGE_NAME + "\t消费者2消费消息:" + message);
    }
}

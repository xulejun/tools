package com.xlj.tools.mq.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.xlj.tools.mq.consumer.BootQueueName.BOOT_WORK_QUEUE_NAME;


/**
 * 工作队列消费者
 *
 * @author xlj
 * @date 2020/12/1 21:00
 */
@Component
public class WorkQueueConsumer {
    /**
     * @description 工作队列消费者1
     * @author xlj
     * @date 2020/12/1 21:03
     */
    @RabbitListener(queuesToDeclare = @Queue(BOOT_WORK_QUEUE_NAME))
    public void receiveMessage1(String message) {
        System.out.println("监听队列：" + BOOT_WORK_QUEUE_NAME + "\t消费者1消费消息:" + message);
    }

    /**
     * @description 工作队列消费者2
     * @author xlj
     * @date 2020/12/1 21:03
     */
    @RabbitListener(queuesToDeclare = @Queue(BOOT_WORK_QUEUE_NAME))
    public void receiveMessage2(String message) {
        System.out.println("监听队列：" + BOOT_WORK_QUEUE_NAME + "\t消费者2消费消息:" + message);
    }
}

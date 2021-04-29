package com.xlj.tools.basis.mq.workqueue;

import com.rabbitmq.client.*;
import com.xlj.tools.util.RabbitMqUtil;

import java.io.IOException;

import static com.xlj.tools.basis.mq.workqueue.WorkQueueProducer.WORK_QUEUE_NAME;


/**
 * 工作（任务)队列生产者
 *
 * @author xlj
 * @date 2020/11/28 18:49
 */
public class WorkQueueConsumer2 {
    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 创建通道
        final Channel channel = connection.createChannel();
        // 通道绑定对应消息队列——生产者和消费者参数严格对应
        /*
        参数1：队列名称，如果队列不存在则自动创建
        参数2：是否持久化队列     false-重启服务器队列消失     true-队列持久化到硬盘
        参数3：exclusive 是否独占队列
        参数4：autoDelete 是否在消费完成后自动删除队列
        参数5：额外附加参数
         */
        channel.queueDeclare(WORK_QUEUE_NAME, false, false, false, null);
        // 一次只接受一条未确认的消息
        channel.basicQos(1);
        // 消费消息
        /*
        参数1：队列名
        参数2：自动确认机制-false不接受平均分配消息
        参数3：消费时的回调接口
         */
        channel.basicConsume(WORK_QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("work-queue消费者2：" + new String(body));
                // 手动确认消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}

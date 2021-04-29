package com.xlj.tools.basis.mq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xlj.tools.util.RabbitMqUtil;

/**
 * 工作（任务)队列生产者
 * 默认情况下，RabbitMQ按顺序将每个消息发送给下一个使用者。每个消费者都能收到相同数量的消息。这种分发方式称为循环
 * @author xlj
 * @date 2020/11/28 18:35
 */
public class WorkQueueProducer {
    public static final String WORK_QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        //  创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 创建一个通道-通过通道传输消息
        Channel channel = connection.createChannel();
        // 通道绑定对应消息队列——生产者和消费者参数严格对应
        /*
        参数1：队列名称，如果队列不存在则自动创建
        参数2：是否持久化队列     false-重启服务器队列消失     true-队列持久化到硬盘
        参数3：exclusive 是否独占队列
        参数4：autoDelete 是否在消费完成后自动删除队列
        参数5：额外附加参数
         */
        channel.queueDeclare(WORK_QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "work_queue消息" + i;
            // 发布消息
            /*
            参数1：交换机名称
            参数2：路由键
            参数3：传递消息额外设置    MessageProperties.PERSISTENT_TEXT_PLAIN传递消息持久化
            参数4：消息的具体内容
            */
            channel.basicPublish("", WORK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("生产\t" + message);
        }
    }
}

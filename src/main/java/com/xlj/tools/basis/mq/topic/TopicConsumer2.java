package com.xlj.tools.basis.mq.topic;

import com.rabbitmq.client.*;
import com.xlj.tools.util.RabbitMqUtil;

import java.io.IOException;

import static com.xlj.tools.basis.mq.topic.TopicProducer.TOPIC_EXCHANGE_NAME;


/**
 * Routing之订阅模型-Topic主题（动态路由）-消费者2
 * 动态路由"#"匹配一个或者多个
 * @author xlj
 * @date 2020/11/29 16:54
 */
public class TopicConsumer2 {
    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 获取通道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, "topic");
        // 创建临时队列
        String queue = channel.queueDeclare().getQueue();
        // 绑定交换机、路由键
        channel.queueBind(queue, TOPIC_EXCHANGE_NAME, "user.#");

        // 消费消息
        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("topic消费者2\t" + new String(body));
            }
        });
    }
}

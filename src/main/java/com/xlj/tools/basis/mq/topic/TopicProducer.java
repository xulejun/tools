package com.xlj.tools.basis.mq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xlj.tools.util.RabbitMqUtil;

/**
 * Routing之订阅模型-Topic主题（动态路由）-生产者
 *
 * @author xlj
 * @date 2020/11/29 16:46
 */
public class TopicProducer {
    /**
     * 主题交换机名称
     */
    public static final String TOPIC_EXCHANGE_NAME = "topic_exchange";

    public static final String TOPIC_ROUTING_KEY = "user";


    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, "topic");

        for (int i = 0; i < 5; i++) {
            String message = "topic消息" + i;
            // 发布消息
            /*
            参数1：交换机名称
            参数2：路由键
            参数3：传递消息额外设置    MessageProperties.PERSISTENT_TEXT_PLAIN传递消息持久化
            参数4：消息的具体内容
             */
            channel.basicPublish(TOPIC_EXCHANGE_NAME, TOPIC_ROUTING_KEY, null, message.getBytes());
            System.out.println("topic生产者\t" + message);
        }

        // 关闭资源
        channel.close();
        connection.close();
    }
}

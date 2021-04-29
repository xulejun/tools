package com.xlj.tools.basis.mq.fanout;

import com.rabbitmq.client.*;
import com.xlj.tools.util.RabbitMqUtil;

import static com.xlj.tools.basis.mq.fanout.FanoutProducer.*;


/**
 * 发布/订阅模型消费者-2
 *
 * @author xlj
 * @date 2020/11/29 15:04
 */
public class FanoutConsumer2 {
    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 创建通道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 创建临时队列
        String queue = channel.queueDeclare().getQueue();
        // 将临时队列绑定交换机
        /*
        参数1：队列名
        参数2：交换机名
        参数3：路由键
         */
        channel.queueBind(queue, EXCHANGE_NAME, "");

        // 消费消息
        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println("订阅模型消费者-2\t" + new String(body));
            }
        });
    }
}

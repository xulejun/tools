package com.xlj.tools.basis.mq.helloworld;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.*;
import com.xlj.tools.util.RabbitMqUtil;

import static com.xlj.tools.basis.mq.helloworld.HelloWorldProducer.*;

/**
 * 点对点模型-消费者
 *
 * @author xlj
 * @date 2020/6/19 17:19
 */
public class HelloWorldConsumer {

    /**
     * 为当前消费者随机取名
     */
    private static final String CUSTOMER_NAME = SIMPLE_QUEUE_NAME + RandomUtil.randomString(2);

    public static void main(String[] args) throws Exception{
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 创建一个通道-通过通道传输消息
        Channel channel = connection.createChannel();
        // 通道绑定对应消息队列
        /*
        参数1：队列名称，如果队列不存在则自动创建
        参数2：是否持久化队列     false-重启服务器队列消失     true-队列持久化到硬盘
        参数3：exclusive 是否独占队列
        参数4：autoDelete 是否在消费完成后自动删除队列
        参数5：额外附加参数
         */
        channel.queueDeclare(SIMPLE_QUEUE_NAME, false, false, false, null);
        System.out.println(CUSTOMER_NAME + "等待接收消息");

        // 消费消息
        /*
        参数1：队列名
        参数2：自动确认机制
        参数3：消费时的回调接口
         */
        channel.basicConsume(SIMPLE_QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                System.out.println(CUSTOMER_NAME + "消费消息\t" + new String(body));
            }
        });
    }
}

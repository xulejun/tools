package com.xlj.tools.basis.mq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xlj.tools.util.RabbitMqUtil;

/**
 * 点对点模型-生产者
 *
 * @author xlj
 * @date 2020/6/19 17:19
 */
public class HelloWorldProducer {

    /**
     * 直连队列名
     */
    public final static String SIMPLE_QUEUE_NAME = "direct_queue";

    public static void main(String[] args) throws Exception {
        // 创建连接
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
        channel.queueDeclare(SIMPLE_QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 10; i++) {
            String message = "direct消息" + i;
            // 发布消息
            /*
            参数1：交换机名称
            参数2：路由键
            参数3：传递消息额外设置    MessageProperties.PERSISTENT_TEXT_PLAIN传递消息持久化
            参数4：消息的具体内容
             */
            channel.basicPublish("", SIMPLE_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("发送消息\t" + message);
        }

        // 关闭连接
        channel.close();
        connection.close();

    }
}

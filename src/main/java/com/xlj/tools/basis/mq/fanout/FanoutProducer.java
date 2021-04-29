package com.xlj.tools.basis.mq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xlj.tools.util.RabbitMqUtil;

/**
 * （广播）发布/订阅模型：一条消息可被多个消费者消费
 *
 * @author xlj
 * @date 2020/11/29 14:54
 */
public class FanoutProducer {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 创建通道
        Channel channel = connection.createChannel();

        // 声明交换机
        /*
        参数1：交换机名称
        参数2：交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 发布消息
        for (int i = 0; i < 10; i++) {
            String message = "发布消息：" + i;
            /*
            参数1：交换机名称
            参数2：路由键
            参数3：传递消息额外设置    MessageProperties.PERSISTENT_TEXT_PLAIN传递消息持久化
            参数4：消息的具体内容
             */
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println(message);
        }

        // 关闭资源
        channel.close();
        connection.close();
    }
}

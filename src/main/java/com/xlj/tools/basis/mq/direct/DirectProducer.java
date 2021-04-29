package com.xlj.tools.basis.mq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xlj.tools.util.RabbitMqUtil;

/**
 * Routing之订阅模型-Direct直连-生产者
 * 不同的消息被不同的队列消费
 *
 * @author xlj
 * @date 2020/11/29 15:38
 */
public class DirectProducer {
    /**
     * 直连交换机名称
     */
    public static final String DIRECT_EXCHANGE = "logs_direct";

    /**
     * 路由键
     */
    public static final String ROUTING_KEY_INFO = "info";
    public static final String ROUTING_KEY_WARN = "warn";
    public static final String ROUTING_KEY_ERROR = "error";

    public static void main(String[] args) throws Exception {
        // 创建连接
        Connection connection = RabbitMqUtil.connectFactory();
        // 获取通道
        Channel channel = connection.createChannel();

        // 声明交换机，1.交换机名称；2.交换机类型
        channel.exchangeDeclare(DIRECT_EXCHANGE, "direct");

        for (int i = 0; i < 3; i++) {
            String infoMessage = "info消息" + i;
            String warnMessage = "warn消息" + i;
            String errorMessage = "error消息" + i;
            // 发布消息
            /*
            参数1：交换机名称
            参数2：路由键
            参数3：传递消息额外设置    MessageProperties.PERSISTENT_TEXT_PLAIN传递消息持久化
            参数4：消息的具体内容
             */
            channel.basicPublish(DIRECT_EXCHANGE, ROUTING_KEY_INFO, null, infoMessage.getBytes());
            channel.basicPublish(DIRECT_EXCHANGE, ROUTING_KEY_WARN, null, warnMessage.getBytes());
            channel.basicPublish(DIRECT_EXCHANGE, ROUTING_KEY_ERROR, null, errorMessage.getBytes());
            System.out.println("发布消息\t" + infoMessage);
            System.out.println("发布消息\t" + warnMessage);
            System.out.println("发布消息\t" + errorMessage);
        }

    }
}

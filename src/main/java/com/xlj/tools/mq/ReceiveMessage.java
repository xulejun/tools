package com.xlj.tools.mq;

import com.rabbitmq.client.Channel;
import com.xlj.tools.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xlj
 * @date 2021/3/17
 */
@Slf4j
@Component
@RabbitListener(queues = {"xlj-queue1"})
public class ReceiveMessage {
    /**
     * 接收消息的类型：Message，T<发送消息的实体类型>，Channel-当前传输数据的通道
     * 1. 可以很多人监听Queue，只要收到消息，队列删除消息，而且只能有一个消息
     * 2. @RabbitListener 监听哪些队列（类+方法）
     * 3. @RabbitHandler 重载区分不同的消息（方法上）
     *
     * @param message
     * @param user
     */
    @RabbitHandler
    public void receiveMessage(Message message, User user, Channel channel) {
        log.info("--------1----------");
        log.info("接收到的消息：{}", message);
        log.info("接收到的内容：{}", user);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 签收消息，非批量模式
            channel.basicAck(deliveryTag,false);
            log.info("消息已签收");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void receiveMessage2(Message message, String str,Channel channel) {
        log.info("--------2----------");
        log.info("接收到的消息：{}", message);
        log.info("接收到的内容：{}", str);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 签收消息，非批量模式
            channel.basicAck(deliveryTag,false);
            log.info("消息已签收");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

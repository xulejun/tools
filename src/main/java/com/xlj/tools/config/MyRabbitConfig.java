package com.xlj.tools.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * RabbitMQ 配置
 *
 * @author xlj
 * @date 2021/3/17
 */
@Slf4j
@Configuration
public class MyRabbitConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 消息Json转换，消息以Json的格式呈现
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制化rabbitTemplate
     */
    @PostConstruct  // 对象创建完成后执行该方法
    public void init() {
        // 抵达Broker确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 当前消息唯一关联的数据
             * @param ack 消息是否收到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("CorrelationData：{}，ack：{}，cause：{}", correlationData, ack, cause);
            }
        });

        // 消息抵达队列的确认回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 投递失败的消息会触发该回调方法
             * @param message 投递失败的消息详情
             * @param replyCode 回复状态码
             * @param replyText 回复文本
             * @param exchange 消息所发送的交换机
             * @param routingKey  消息所发送的路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("Fail Message:{},ReplyCode:{},replyText:{},exchange:{},routingKey:{}", message, replyCode, replyText, exchange, routingKey);
            }
        });
    }

    // TODO 队列绑定
}

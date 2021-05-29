package com.xlj.tools.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kafka整合——接受消息
 *
 * @author xlj
 * @date 2021/5/29
 */
@Slf4j
@Component
public class KafkaReceiveMessage {
    /**
     * 消息消费：一次接受多个消息
     *
     * @param record
     */
    @SendTo(value = "test-topic1")     // 消息转发，由test-topic处理完之后再转发给test-topic1，测试无果
    // 监听topic，异常处理，消息过滤
    @KafkaListener(topics = {"test-topic"}, errorHandler = "consumerAwareErrorHandler", containerFactory = "filterContainerFactory")
    public void receiveMessage(List<String> record) {
        for (String message : record) {
            log.info("test-topic接受消息：消息：【{}】", message);
        }
//        throw new Exception("kafka简单消费-模拟异常");
    }

    @KafkaListener(topics = {"test-topic1"}, errorHandler = "consumerAwareErrorHandler", containerFactory = "filterContainerFactory")
    public void receiveMessage1(List<String> record) {
        for (String message : record) {
            log.info("test-topic1接受消息：消息：【{}】，", message);
        }
//        throw new Exception("kafka简单消费-模拟异常");
    }
}

package com.xlj.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

/**
 * kafka整合测试
 *
 * @author xlj
 * @date 2021/5/27
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ToolsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaTest {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    String topic = "test-topic";

    /**
     * 创建topic
     */
    @Test
    public void createTopic() {
        // topic、分区数、分区副本数
        // 修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小
        NewTopic topic = new NewTopic("test-topic1", 8, (short) 2);
        log.info("创建topic：{}", topic.toString());
    }

    /**
     * 发送简单消息
     */
    @Test
    public void sendMessage() {
        String message = "hello，我是xlj";
        kafkaTemplate.send(topic, message);
        log.info("Kafka发送消息：{}", message);
    }

    /**
     * 发送消息失败回调
     */
    @Test
    public void sendMessageCallback() {
        String message = "hello，我是xlj，sendMessageCallback";
        kafkaTemplate.send(topic, message).addCallback(success -> {
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            log.info("发送消息成功：{}-{}-{}，消息【{}】", topic, partition, offset, message);
        }, failure -> {
            log.info("发送消息失败：{}", failure.getMessage());
        });
    }

    /**
     * 发送消息事务提交
     */
    @Test
    public void sendMessageTransaction() {
        String message = "hello，我是xlj，sendMessageTransaction";

        // 声明事务：后面报错消息不会发出去，    报错：Producer factory does not support transactions
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(topic, message);
            throw new RuntimeException("fail");
        });
    }

}

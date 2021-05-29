package com.xlj.tools.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;

/**
 * kafka配置
 *
 * @author xlj
 * @date 2021/5/29
 */
@Slf4j
@Configuration
public class KafkaConfig {
    @Autowired
    private ConsumerFactory consumerFactory;

    /**
     * 消息异常处理
     *
     * @return
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, exception, consumer) -> {
            log.info("kafka消费异常：消息【{}】，消费者【{}】，异常：", message.getPayload(), consumer);
            return null;
        };
    }

    /**
     * 消息过滤器
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (consumerRecord.toString().contains(" ")) {
                return false;
            }
            // 返回true消息则被过滤
            log.warn("消息被过滤了：{}", consumerRecord);
            return true;
        });
        return factory;
    }
}

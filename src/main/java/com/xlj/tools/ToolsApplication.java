package com.xlj.tools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author xlj
 */
@EnableKafka
@EnableRabbit   // 只是在监听消息时需要
@SpringBootApplication
@MapperScan("com.xlj.tools.dao")
//@PropertySource(value = {"classpath:kafka.properties"}, encoding = "UTF-8")     // 导入配置文件，与@Value结合使用，取值
//@ImportResource(locations = "classpath:kafka.properties")       // 加载配置文件
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsApplication.class, args);
    }

}

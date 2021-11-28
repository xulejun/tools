package com.xlj.tools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author xlj
 */
//@EnableKafka
@EnableAsync    // 开启异步
@EnableRabbit   // 只是在监听消息时需要
@SpringBootApplication
@EnableRedisHttpSession     // 整合Redis作为session存储
@MapperScan("com.xlj.tools.dao")
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.xlj.tools.mq.*")})   // 扫描包过滤
//@PropertySource(value = {"classpath:kafka.properties"}, encoding = "UTF-8")     // 导入配置文件，与@Value结合使用，取值
//@ImportResource(locations = "classpath:kafka.properties")       // 加载配置文件
public class ToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToolsApplication.class, args);
    }

}

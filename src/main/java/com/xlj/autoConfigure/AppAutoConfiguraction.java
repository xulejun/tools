package com.xlj.autoConfigure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

/**
 * 应用自动配置
 */
//当前类不能在basePackages描到的包下, 否则会出现死循环
@Configuration
@ComponentScan("com.xlj.tools")
//@PropertySource(value = {"classpath:fdfs_client.properties","classpath:bainian.properties"}, ignoreResourceNotFound = true, encoding = "UTF-8")
@MapperScan({"com.xlj.tools.dao"})
//@ImportResource(locations = { "classpath:META-INF/spring/*.xml" }) //dubbo发布的接口
public class AppAutoConfiguraction {
}

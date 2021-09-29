package com.xlj.tools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * spring-session 自定义配置
 *
 * @author legend xu
 * @date 2021/9/29
 */
@Configuration
public class SessionConfig {

    /**
     * cookie序列化：解决domain域问题，放大domain作用域
     *
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setDomainName(".legend.com");
        cookieSerializer.setCookieName("LEGENDSESSION");
        return cookieSerializer;
    }

    /**
     * 存放redis-session序列化问题
     *
     * @return
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}

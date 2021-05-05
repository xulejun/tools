package com.xlj.tools.config;

import com.xlj.tools.interceptor.SourceAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器链
 * 登录拦截验证时打开此类
 *
 * @author xlj
 * @date 2021/4/29
 */
//@Configuration
//public class InterceptorTrainConfig implements WebMvcConfigurer {
//    /**
//     * 把拦截器添加到拦截器链中
//     *
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SourceAccessInterceptor()).addPathPatterns("/**");
//    }
//}

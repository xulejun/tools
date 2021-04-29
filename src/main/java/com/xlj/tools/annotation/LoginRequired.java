package com.xlj.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/***
 * @description: 自定义注解
 *               + 拦截器  实现简单的登录校验
 * @author xlj
 * @date 2021/4/29 9:19
 */
@Target(ElementType.METHOD)     // 注解用于方法上
@Retention(RetentionPolicy.RUNTIME)     // 保留到运行时，可通过注解来获取
public @interface LoginRequired {

    // 注解的参数：基本类型 + 参数名（）
//    String value();
//    int age() default 12;

}

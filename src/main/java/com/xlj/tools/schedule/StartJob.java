package com.xlj.tools.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务：
 * 1. @EnableScheduling 开启定时任务
 * 2. @Scheduled开启一个定时任务
 * 3. 自动配置类 TaskSchedulingAutoConfiguration
 * 异步任务：
 * 1. @EnableAsync 开启异步任务功能
 * 2. @Async 方法异步执行
 * 3. 自动配置类 TaskExecutionAutoConfiguration（线程池大小：默认为8，最大为integer的max，最好手动配置）
 *
 * @author xlj
 * @date 2021/4/29
 */
@Slf4j
//@Component
public class StartJob {
    Integer i = 0;

    @PostConstruct
    public void init() {
        log.info("定时任务开始了");
    }

    /**
     * 定时任务不应该阻塞（不需要等待业务逻辑执行完后再执行），默认是阻塞的
     * 1. 可以让业务运行以异步的方式，自己提交到线程池
     * CompletableFuture.runAsync(() -> {
     * // 业务逻辑
     * },executor);
     * 2.支持定时任务线程池，在配置文件中配置，默认为1（有时不起作用）
     * 3. 让定时任务异步执行
     *
     * @throws InterruptedException
     */
//    @Async
    @Scheduled(cron = "0/1 * * * * ? ")     // cron表达式：当前方法执行完毕后，再过1s后执行此方法
    public void startCron() throws InterruptedException {
        log.info("cron表达式形式：{}", i.toString());
        TimeUnit.SECONDS.sleep(1L);
        i++;
    }

    @Scheduled(fixedDelay = 1000)   // fixedDelay：当前方法执行完毕后，再过1s后执行此方法
    public void startFixedDelay() throws InterruptedException {
        log.info("fixedDelay形式：{}", i.toString());
        TimeUnit.SECONDS.sleep(1L);
    }
}

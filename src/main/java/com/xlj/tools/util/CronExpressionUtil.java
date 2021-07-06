package com.xlj.tools.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Cron表达式实现定时
 *
 * @author xlj
 * @date 2021/7/5
 */
@Slf4j
@Component
public class CronExpressionUtil {
    /**
     * 存储SpiderJob的ID：对应的定时任务
     */
    private final ConcurrentHashMap<Long, ScheduledFuture> tasksMap = new ConcurrentHashMap<>();

    @Bean
    public ThreadPoolTaskScheduler taskExecutor() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(1000);
        executor.setThreadNamePrefix("cronTask-");
        return executor;
    }

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 定时任务开启
     *
     * @param id
     * @param cron
     */
    public void start(Long id, String cron) {
        CronTrigger cronTrigger = null;
        try {
            cronTrigger = new CronTrigger(cron);
        } catch (Exception e) {
            log.error("corn表达式解析异常:cron[{}]", cron);
        }
        // 定时执行
        AtomicInteger i = new AtomicInteger();
        ScheduledFuture schedule = threadPoolTaskScheduler.schedule(() -> {
            log.info("hello:{}", i.getAndIncrement());
        }, cronTrigger);
        tasksMap.put(id, schedule);
    }

    /**
     * 定时任务关闭
     *
     * @param id
     */
    public void stop(Long id) {
        ScheduledFuture scheduledFuture = tasksMap.get(id);
        scheduledFuture.cancel(true);
        tasksMap.remove(id);
    }
}

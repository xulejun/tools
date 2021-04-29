package com.xlj.tools.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务-任务调度
 *
 * @author xlj
 * @date 2021/4/29
 */
@Slf4j
@Configuration
//@EnableScheduling
public class StartJob {
    Integer i = 0;

    @PostConstruct
    public void init() {
        log.info("定时任务开始了");
    }

    @Scheduled(cron = "0/1 * * * * ? ")     // cron表达式：当前方法执行完毕后，再过1s后执行此方法
    public void startCron() throws InterruptedException {
        log.info("cron表达式形式：{}", i.toString());
        TimeUnit.SECONDS.sleep(1L);
        i++;
    }

    @Scheduled(fixedDelay = 1000)   // fixedDelay：当前方法执行完毕后，再过1s后执行此方法
    public void startFixedDelay() throws InterruptedException {
        log.info("fixedDelay形式：{}",i.toString());
        TimeUnit.SECONDS.sleep(1L);
    }
}

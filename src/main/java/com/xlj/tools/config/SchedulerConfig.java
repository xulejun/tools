package com.xlj.tools.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置
 *
 * @author xlj
 * @date 2021/5/22
 */
@EnableAsync    // 异步不阻塞：不需要等待业务逻辑执行的时间
@Configuration
@EnableScheduling
public class SchedulerConfig {
}

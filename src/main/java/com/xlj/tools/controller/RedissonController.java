package com.xlj.tools.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * redisson分布式锁接口测试
 *
 * @author xlj
 * @date 2021/5/20
 */
@Slf4j
@RestController
public class RedissonController {
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/redisson")
    public String redisson() {
        RLock lock = redissonClient.getLock("my_lock");
        try {
            /*
            1. 加锁，阻塞式等待，默认加锁时间为30s，如果业务执行时间超过30s，锁会自动续期（30s，看门狗的默认时间），
                如果业务提前执行完毕，锁自动过期会被删除，不会产生死锁的情况（看门狗）
            2. 只要占锁成功，就会启动一个定时任务（底层源码：看门狗的默认时间 / 3 = 10s ），重新给锁设置过期时间（恢复到30s）
            3. 只要业务运行完成，就不会给锁自动续期
            4. 底层走的还是给redis发lua脚本去执行，保证原子性
            */
            lock.lock();
            /*
            1. 指定过期时间，业务执行时间 > 过期时间，不会自动续期，当其他线程拿到该锁，再释放时，会报错
            2. 实战推荐使用该方法：倘若业务代码执行超出指定时间，就认为它异常，手动释放锁，也省去了续期的操作
             */
            lock.lock(10,TimeUnit.SECONDS);
            // 业务代码
            log.info("线程 {} 正在执行业务代码", Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(30);
        } catch (Exception e) {
            log.warn("异常信息");
        } finally {
            lock.unlock();
            log.info("线程 {} 已解锁", Thread.currentThread().getId());
        }
        return "hello";
    }
}

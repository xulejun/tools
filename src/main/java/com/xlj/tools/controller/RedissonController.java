package com.xlj.tools.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private RedisTemplate redisTemplate;

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
//            lock.lock();
            /*
            1. 指定过期时间，业务执行时间 > 过期时间，不会自动续期，当其他线程拿到该锁，再释放时，会报错
            2. 实战推荐使用该方法：倘若业务代码执行超出指定时间，就认为它异常，手动释放锁，也省去了续期的操作
             */
            lock.lock(10, TimeUnit.SECONDS);
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

    /**
     * 保证一定能获取最新数据，修改期间，写锁就是一个排它锁（互斥锁）。读锁是一个共享锁
     * 写锁没释放，读锁就一直等待
     * 只要有写锁存在，就必须等待，无论是 读+写 还是 写+读
     *
     * @return
     */
    @GetMapping("/writeLock")
    public String writeLock() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("read-write-lock");
        RLock rLock = lock.writeLock();
        String uuid = "";
        // 加写锁
        rLock.lock();
        try {
            uuid = IdUtil.simpleUUID();
            // 写数据处理时间，读数据一直等待
            TimeUnit.SECONDS.sleep(10);
            // 写数据
            redisTemplate.opsForValue().set("uuid", uuid);
        } catch (Exception e) {
            log.warn("写锁异常");
        } finally {
            rLock.unlock();
        }
        return uuid;
    }

    @GetMapping("/readLock")
    public String readLock() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("read-write-lock");
        RLock rLock = lock.readLock();
        // 加读锁
        rLock.lock();
        String uuid = "";
        try {
            // 读数据
            uuid = (String) redisTemplate.opsForValue().get("uuid");
        } catch (Exception e) {
            log.warn("读锁异常");
        } finally {
            rLock.unlock();
        }
        return uuid;
    }

    /**
     * 分布式闭锁：闭锁数量完全减少，释放锁，不然持续等待
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-lock");
        // 闭锁设置数量
        countDownLatch.trySetCount(5);
        // 等待闭锁完成
        countDownLatch.await();
        return "校门已锁";
    }

    @GetMapping("/holiday/{id}")
    public String holiday(@PathVariable Integer id) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("count-down-lock");
        // 计数减1
        countDownLatch.countDown();
        return id + "班放假了";
    }
}

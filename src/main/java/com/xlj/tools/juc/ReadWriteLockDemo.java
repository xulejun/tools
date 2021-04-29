package com.xlj.tools.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock读写锁
 *
 * @author xlj
 * @date 2020/10/23 23:13
 */
class MyCache {
    private Map map = new HashMap();
    // 创建读写锁
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
    * @description 写操作
    * @author xlj
    * @date 2020/11/9 22:14
    */
    public void put(String key, String value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "写入数据");
            map.put(key, value);
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println(Thread.currentThread().getName() + "写入完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
    * @description 读操作
    * @author xlj
    * @date 2020/11/9 22:14
    */
    public void get(String key) {
        readWriteLock.readLock().lock();
        try {
            TimeUnit.MILLISECONDS.sleep(1);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "获取数据\t" + o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        // 创建5个线程依次写
        for (int i = 1; i <= 5; i++) {
            int temp = i;
            new Thread(() -> {
                myCache.put(String.valueOf(temp), String.valueOf(temp));
            }, String.valueOf(temp)).start();
        }

        // 创建5个线程依次读
        for (int i = 1; i <= 5; i++) {
            int temp = i;
            new Thread(() -> {
                myCache.get(String.valueOf(temp));
            }, String.valueOf(temp)).start();
        }
    }
}

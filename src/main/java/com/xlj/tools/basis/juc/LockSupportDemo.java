package com.xlj.tools.basis.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author legend xu
 * @date 2022/12/28
 */
public class LockSupportDemo {
    public static void main(String[] args) throws Exception {
        // objectWaitNotify();
        // conditionAwaitSignal();
        lockSupportPark();
    }

    /**
     * 无需先后顺序
     * 无锁块要求，代码简洁
     * 许可证不会累计，只有一个，不允许多对多
     */
    private static void lockSupportPark() {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "\t 线程已启动");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t 线程被唤醒");
        }, "t1");
        t1.start();

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t 线程发起通知");
        }, "t2").start();
    }

    /**
     * Condition 中的线程等待和唤醒方法，需要先获取锁
     * 一定要先 await 后 signal，线程才能被唤醒
     * @throws InterruptedException
     */
    private static void conditionAwaitSignal() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 线程已启动");
            lock.lock();
            try {
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t 线程被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t 线程发出通知");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * wait 和 notify 方法必须要在同步块或者方法里面， 且成对出现使用
     * 顺序上先 wait 后 notify，否则无法唤醒
     *
     * @throws InterruptedException
     */
    private static void objectWaitNotify() throws InterruptedException {
        Object objectLock = new Object();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 线程已启动");
            synchronized (objectLock) {
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t 线程已被唤醒");
            }
        }, "t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t 线程发出通知");
            }
        }, "t2").start();
    }
}

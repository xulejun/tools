package com.xlj.tools.juc;

import java.util.concurrent.CountDownLatch;

/**
 * JUC倒数锁
 * <p>
 * 场景：当教室人都走完了，班长才关门
 *
 * @author xlj
 * @date 2020/10/22 22:57
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        // 未加倒数锁前，不等待线程，直接锁门
//        closeDoor();

        // 倒数锁
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t离开教室");
                // 当前数逐个减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        // 倒数锁中没有数唤醒
        countDownLatch.await();
        System.out.println("班长关门");

    }

    public static void closeDoor() {
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t离开教室");
            }, String.valueOf(i)).start();
        }
        System.out.println("班长关门");
    }
}

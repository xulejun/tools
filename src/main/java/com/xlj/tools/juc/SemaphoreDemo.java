package com.xlj.tools.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore信号灯样例
 * 场景：6辆车抢占3个车位
 *
 * @author xlj
 * @date 2020/10/23 21:52
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        // Semaphore容量数
        Semaphore semaphore = new Semaphore(3);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                try {
                    // Semaphore抢占，库存减一
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "抢占到了车位");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Semaphore释放，恢复
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + "离开车位");
                }
            }, String.valueOf(i)).start();
        }
    }
}

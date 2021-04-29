package com.xlj.tools.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier类样例：与CountDownLatch相反，这个作加法
 * 场景：7颗龙珠召唤神龙
 *
 * @author xlj
 * @date 2020/10/23 21:29
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });
        for (int i = 1; i < 8; i++) {
            int a = i;
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "收集到\t" + a + "颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}

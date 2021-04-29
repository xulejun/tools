package com.xlj.tools.basis.jvm;

import java.util.concurrent.TimeUnit;

/**
 * Volatile可见性，不保证原子性，禁止指令重排序
 *
 * @author xlj
 * @date 2020/11/8 15:12
 */

class MyNumber {
//    int number = 10;
    volatile int number = 10;

    public void doNumber() {
        this.number = 1024;
    }
}

public class VolatileDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is running");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myNumber.doNumber();
        }, "A").start();

        // 没加volatile关键字，不可见，其他线程接收不到通知，持续循环等待
        while (myNumber.number == 10) {
        }

        // number改变时结束
        System.out.println(Thread.currentThread().getName() + " is over");
    }


}

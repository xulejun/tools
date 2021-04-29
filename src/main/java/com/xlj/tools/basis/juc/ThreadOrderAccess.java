package com.xlj.tools.basis.juc;

/**
 * Condition精确通知
 * 实现：三个线程依次输出，A打印5次，B打印10次，C打印15次；重复10次
 *
 * @author xlj
 * @date 2020/10/18 22:53
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xlj
 * @description 资源类
 * @date 2020/10/18 22:54
 */
class ReSource {
    // 通知序号
    public int number = 1;
    // 创建Lock
    private Lock lock = new ReentrantLock();
    // 创建3个Condition
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    // A方法-打印5次
    public void print5() {
        lock.lock();
        // 判断
        while (number != 1) {
            try {
                condition1.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 干活
        for (int i = 1; i <= 5; i++) {
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        }
        // 通知，修改标记位，实现精确通知
        number = 2;
        condition2.signal();
        lock.unlock();
    }
    // B方法-打印10次
    public void print10() {
        lock.lock();
        // 判断
        while (number != 2) {
            try {
                condition2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 干活
        number = 3;
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        }
        // 通知
        condition3.signal();
        lock.unlock();
    }
    // C方法-打印15次
    public void print15() {
        lock.lock();
        // 判断
        while (number != 3) {
            try {
                condition3.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 干活
        number = 1;
        for (int i = 1; i <= 15; i++) {
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        }
        // 通知
        condition1.signal();
        lock.unlock();
    }
}

public class ThreadOrderAccess {
    public static void main(String[] args) {
        ReSource reSource = new ReSource();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reSource.print5();
            }
        },"A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reSource.print10();
            }
        },"B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                reSource.print15();
            }
        },"C").start();

    }
}

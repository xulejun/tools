package com.xlj.tools.basis.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 实现一个自旋锁：通过 CAS 操作完成自旋锁
 * A 线程先进来调用 lock 方法自己持有锁 5s，B 随后进来后发现当前有线程持有锁，所以只能通过自旋等待，直到 A 释放锁之后 B 随后抢到
 *
 * @author legend xu
 * @date 2023/1/3
 */
public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock(){
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t come in");
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println(thread.getName() + "\t in spin lock");
        }
    }

    public void unlock(){
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(thread.getName() + "\t unlock");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() ->{
            spinLockDemo.lock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.unlock();
        },"A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(999);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() ->{
            spinLockDemo.lock();

            spinLockDemo.unlock();
        },"B").start();

    }
}

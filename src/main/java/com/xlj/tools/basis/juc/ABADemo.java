package com.xlj.tools.basis.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS 缺点：ABA 问题
 *
 * @author legend xu
 * @date 2023/1/3
 */
public class ABADemo {
    static AtomicInteger atomicInteger = new AtomicInteger(0);
    /**
     * 时间戳原子类引用
     */
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(0, 0);


    public static void main(String[] args) {
        // abaHappen();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "初始值：" + stampedReference.getReference() + ";版本号：" + stampedReference.getStamp());
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stampedReference.compareAndSet(0, 1, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "第一次修改：" + stampedReference.getReference() + ";版本号：" + stampedReference.getStamp());
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stampedReference.compareAndSet(1, 0, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "第二次修改：" + stampedReference.getReference() + ";版本号：" + stampedReference.getStamp());
        }, "t3").start();

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "初始值：" + stampedReference.getReference() + ";版本号：" + stamp);
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(stampedReference.compareAndSet(0, 2023, stamp, stamp + 1));
            System.out.println(Thread.currentThread().getName() + "最终值：" + stampedReference.getReference() + ";版本号：" + stamp);
        }, "t4").start();
    }

    private static void abaHappen() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "初始值：" + atomicInteger.get());
            atomicInteger.compareAndSet(0, 1);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atomicInteger.compareAndSet(1, 0);
        }, "t1").start();


        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "初始值：" + atomicInteger.get());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(atomicInteger.compareAndSet(0, 2023) + "\t" + atomicInteger.get());
        }, "t2").start();
    }
}

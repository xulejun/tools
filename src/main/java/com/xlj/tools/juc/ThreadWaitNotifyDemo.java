package com.xlj.tools.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1.两个线程分别交替操作同一个资源类,操作10次
 * 2.判断、干活、通知
 * 3.多线程交互中，必须要防止多线程的虚假唤醒，也即为（判断只能用while，不能用if）
 *
 * Lock类替换synchronize关键字，Condition的signaAll方法替换Object的notifyAll方法
 *
 * @author xlj
 * @date 2020/10/17 18:15
 */

class Source {
    public int initNum = 0;
    // 新版-利用Lock类替换synchronize关键字，Condition的
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increase() throws InterruptedException {
        // 锁住当前方法，方法前无需再添加synchronize关键字
        lock.lock();
        // 利用while循环解决多线程的虚假唤醒
        while (initNum != 0) {
            // Condition的await方法替换Object的wait方法
            condition.await();
        }
        initNum++;
        System.out.println(Thread.currentThread().getName() + "\t" + initNum);
        // Condition的signaAll方法替换Object的notifyAll方法
        condition.signalAll();
        // 关闭当前锁
        lock.unlock();
    }

    public void decrease() throws InterruptedException {
        // 锁住当前方法，方法前无需再添加synchronize关键字
        lock.lock();
        // 利用while循环解决多线程的虚假唤醒
        while (initNum == 0) {
            // Condition的await方法替换Object的wait方法
            condition.await();
        }
        initNum--;
        System.out.println(Thread.currentThread().getName() + "\t" + initNum);
        // Condition的signaAll方法替换Object的notifyAll方法
        condition.signalAll();
        // 关闭当前锁
        lock.unlock();
    }

    // 增加——添加synchronized同步关键字,保证其他方法无法执行
//    public synchronized void increase() throws InterruptedException {
//        while (initNum != 0) {
//            this.wait();
//        }
//        initNum++;
//        System.out.println(Thread.currentThread().getName()+"\t"+initNum);
//        this.notifyAll();
//    }
//    // 减少——添加synchronized同步关键字
//    public synchronized void decrease() throws InterruptedException {
//        while (initNum == 0) {
//            this.wait();
//        }
//        initNum--;
//        System.out.println(Thread.currentThread().getName()+"\t"+initNum);
//        this.notifyAll();
//    }
}

/**
 * @author xlj
 * @description 高内聚低耦合，线程操作资源类
 * @date 2020/10/17 18:24
 */
public class ThreadWaitNotifyDemo {
    public static void main(String[] args) {
        // 创建资源类对象
        Source source = new Source();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.increase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.decrease();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.increase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    source.decrease();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();

    }

}

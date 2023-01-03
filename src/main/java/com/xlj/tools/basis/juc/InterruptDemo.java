package com.xlj.tools.basis.juc;

import java.util.concurrent.TimeUnit;

/**
 * 线程中断 demo
 *
 * @author legend xu
 * @date 2022/12/28
 */
public class InterruptDemo {
    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "中断标志位为 true，线程中断");
                    break;
                }
                // 中断尝试调用 sleep 方法，是否会出现无限循环
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    // 需要在异常处，在调用一次中断
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "线程正在运行");
            }
        }, "t1");
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() ->{
            // 设置线程的中断状态为 true，发起一个协商而不会立刻停止线程
            t1.interrupt();
        },"t2").start();
    }
}

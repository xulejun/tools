package com.xlj.tools.basis.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Callable接口案例
 *
 * @author XLJ
 * @date 2020/10/22
 */

/**
 * Callable与Runnable接口区别：
 * 1.Runnable重写run方法，Callable重写call方法
 * 2.泛型，Callable有返回参数类型
 * 3.重写call方法抛出异常
 * <p>
 * 实现：
 * 1.Thread类中没有Callable的构造方法，需要通过“中介”去实现
 * 2.FutureTask类实现了Runnable接口，同时有Callable的构造方法
 * 3.通过FutureTask类实现
 * 4.FutureTask.get方法获得Callable的返回参数
 *
 * @author XLJ
 * @date 2020/10/22 19:47
 */
class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("come in call of Callable");
        TimeUnit.SECONDS.sleep(3);
        return 1024;
    }
}

public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new MyThread());

        // FutureTask在被多个线程调用时，只会被调用一次
        new Thread(futureTask,"A").start();
        new Thread(futureTask,"B").start();

        System.out.println(futureTask.get());
        System.out.println("计算完成");
    }
}

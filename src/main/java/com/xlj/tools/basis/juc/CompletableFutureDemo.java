package com.xlj.tools.basis.juc;

import java.util.concurrent.*;

/**
 * CompletableFuture 四个静态方法的使用
 * 尽量不要使用 new 来构建 CompletableFuture 对象的使用
 * 1. 有返回值  2. 没有返回值
 *
 * @author legend xu
 * @date 2022/12/18
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        // completableFutureStaticUse();

        ExecutorService pool = Executors.newFixedThreadPool(3);
        // 完成时回调
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }, pool).whenComplete((value, e) -> {
            if (e == null) {
                System.out.println("计算完成，更新：" + value);
            }
        }).exceptionally(e -> {
            System.out.println("异常情况：" + e);
            return null;
        });

        System.out.println("main 线程去忙其他业务");

        // 主线程不要立刻结束，否则异步线程虽然执行，但是无法输出结果（设置了1s延迟），因此暂停一会
        TimeUnit.SECONDS.sleep(3);

    }

    /**
     * completableFuture 四种静态方法的使用
     */
    private static void completableFutureStaticUse() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // 1. runAsync 没有返回值，默认的 ForkJoinPool.commonPool()
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(completableFuture.get());

        // 2. runAsync 没有返回值，使用指定的 pool
        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, pool);
        System.out.println(completableFuture1.get());

        // 3. supplyAsync 有返回值，默认的 ForkJoinPool.commonPool()
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello,supplyAsync";
        });
        System.out.println(supplyAsync.get());

        // 4. supplyAsync 有返回值，使用指定的 pool
        CompletableFuture<String> supplyAsync1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello,supplyAsync + pool";
        }, pool);
        System.out.println(supplyAsync1.get());

        pool.shutdown();
    }
}

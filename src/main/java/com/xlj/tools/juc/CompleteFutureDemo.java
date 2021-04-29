package com.xlj.tools.juc;

import java.util.concurrent.CompletableFuture;

/**
 * 异步回调
 *
 * @author xlj
 * @date 2020/10/28 22:18
 */
public class CompleteFutureDemo {
    public static void main(String[] args) throws Exception {
        // 异步无返回值
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行完毕，无返回值");
        });
        runAsync.get();

        // 异步回调，有返回值-----supplyAsync(供给型接口---无传入参数，有返回值)
        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行完毕，有返回值");
//            int a = 10/0;
            return 1024;
        });
        supplyAsync.whenComplete((t, u) -> {
            System.out.println("********" + t);
            System.out.println("********" + u);
        }).exceptionally(t -> {
            System.out.println("*********" + t.getMessage());
            return 404;
        }).get();

    }
}

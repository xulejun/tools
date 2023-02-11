package com.xlj.tools.basis.juc;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Data;

import java.util.concurrent.*;

/**
 * ThreadLocal 使用
 * 需求1：5个销售卖房，统计总销售额
 * 需求2：使用 ThreadLocal 统计个人销售额
 *
 * @author legend xu
 * @date 2023/2/11
 */
@Data
class Sale {
    private int num;

    public synchronized void add() {
        ++num;
    }

    ThreadLocal<Integer> threadLocalNum = ThreadLocal.withInitial(() -> 0);

    public void addByThreadLocal() {
        threadLocalNum.set(1 + threadLocalNum.get());
    }

}

public class ThreadLocalDemo {
    public static void main(String[] args) throws Exception {
        int saleCount = 5;
        Sale sale = new Sale();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CountDownLatch countDownLatch = new CountDownLatch(saleCount);
        for (int i = 0; i < saleCount; i++) {
            pool.submit(() -> {
                int count = RandomUtil.randomInt(1, 5);
                try {
                    for (int j = 0; j < count; j++) {
                        sale.add();
                        sale.addByThreadLocal();
                    }
                    // 统计个人销售数量
                    System.out.println(Thread.currentThread().getName() + "\t 销售：" + sale.getThreadLocalNum().get());
                } finally {
                    // 阿里规约：必须回收自定义的 ThreadLocal 变量，尤其在线程池场景下，线程经常会被复用，不清理可能会 影响后续业务逻辑 和 造成内存泄露问题
                    sale.threadLocalNum.remove();
                    countDownLatch.countDown();
                }
            });
        }
        //TimeUnit.SECONDS.sleep(1);
        // 无需等待，替换睡眠时间
        countDownLatch.await();
        // 统计总销售数量
        System.out.println("总共销售了：" + sale.getNum());
        pool.shutdown();
    }
}

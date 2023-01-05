package com.xlj.tools.basis.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * 大数据高并发下（5000 w），测试 4 种方法的性能
 * synchronized 、 AtomicLong 、 LongAdder 、 LongAccumulator
 *
 * @author legend xu
 * @date 2023/1/5
 */
@Slf4j
public class LongAdderDemo {
    int num = 0;

    public synchronized void plusBySynchronized() {
        num++;
    }

    AtomicLong atomicLong = new AtomicLong(0);

    public void plusByAtomicLong() {
        atomicLong.getAndIncrement();
    }

    LongAdder longAdder = new LongAdder();

    public void plusByLongAdder() {
        longAdder.add(1);
    }

    LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);

    public void plusByLongAccumulator() {
        longAccumulator.accumulate(1);
    }


    public static void main(String[] args) throws Exception {
        // 50 * 100 0000 模拟点击 5000 w 次，测试 4 种方法的性能
        int threadNum = 50;
        int count = 1000000;

        LongAdderDemo demo = new LongAdderDemo();
        long start;
        long end;

        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < count; j++) {
                        demo.plusBySynchronized();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, "plusBySynchronized" + i).start();
        }
        countDownLatch.await();
        end = System.currentTimeMillis();
        log.info("plusBySynchronized 方法计算结果为：{}，耗时：{} ms", demo.num, end - start);

        CountDownLatch countDownLatch1 = new CountDownLatch(threadNum);;
        start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < count; j++) {
                        demo.plusByAtomicLong();
                    }
                } finally {
                    countDownLatch1.countDown();
                }
            }, "plusByAtomicLong" + i).start();
        }
        countDownLatch1.await();
        end = System.currentTimeMillis();
        log.info("plusByAtomicLong 方法计算结果为：{}，耗时：{} ms", demo.atomicLong, end - start);

        CountDownLatch countDownLatch2 = new CountDownLatch(threadNum);;
        start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < count; j++) {
                        demo.plusByLongAdder();
                    }
                } finally {
                    countDownLatch2.countDown();
                }
            }, "plusByLongAdder" + i).start();
        }
        countDownLatch2.await();
        end = System.currentTimeMillis();
        log.info("plusByLongAdder 方法计算结果为：{}，耗时：{} ms", demo.longAdder, end - start);

        CountDownLatch countDownLatch3 = new CountDownLatch(threadNum);;
        start = System.currentTimeMillis();
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < count; j++) {
                        demo.plusByLongAccumulator();
                    }
                } finally {
                    countDownLatch3.countDown();
                }
            }, "plusByLongAccumulator" + i).start();
        }
        countDownLatch3.await();
        end = System.currentTimeMillis();
        log.info("plusByLongAccumulator 方法计算结果为：{}，耗时：{} ms", demo.longAccumulator, end - start);
    }

}

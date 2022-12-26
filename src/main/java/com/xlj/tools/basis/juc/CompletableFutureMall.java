package com.xlj.tools.basis.juc;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 案例：电商比价需求
 * 1. 需求： 同一款产品，同时搜索出同款产品在各大电商平台的售价
 * 2. 输出： 希望同款产品的在不同地方的价格清单列表，返回一个 List<String>
 * 3. 技术： 函数式编程 + 链式编程 + Stream 流式计算
 *
 * @author legend xu
 * @date 2022/12/26
 */
public class CompletableFutureMall {
    static List<NetMall> list = Arrays.asList(
            new NetMall("jd"),
            new NetMall("taobao"),
            new NetMall("dangdang"));

    public static List<String> getPrice(List<NetMall> list, String productName) {
        return list.stream().map(netMall ->
                String.format(productName + " in %s price is %.2f", netMall.getNetMallName(), netMall.calcPrice(productName))
        ).collect(Collectors.toList());
    }

    /**
     * 多线程异步查询
     *
     * @param list
     * @param produceName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String produceName) {
        return list.stream().map(netMall ->
                CompletableFuture.supplyAsync(() ->
                        String.format(produceName + " in %s price is %.2f", netMall.getNetMallName(), netMall.calcPrice(produceName))))
                .collect(Collectors.toList())
                .stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        getPrice(list, "mysql").forEach(System.out::println);
        long end = System.currentTimeMillis();
        System.out.println(end - start + "毫秒");

        long start1 = System.currentTimeMillis();
        getPriceByCompletableFuture(list, "mysql").forEach(System.out::println);
        long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1 + " 毫秒");
    }
}

@Getter
class NetMall {
    private String netMallName;

    public NetMall(String netMallName) {
        this.netMallName = netMallName;
    }

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
    }
}
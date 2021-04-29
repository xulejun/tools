package com.xlj.tools.basis.patterns.factory.base;


import java.util.Scanner;


/**
 * 订购披萨入口
 *
 * @author xlj
 * @date 2020/12/15 21:54
 */
public class PizzaStore {
    private static final String SH = "sh";
    private static final String BJ = "bj";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("请问您想点哪个地区的披萨：");
            String pizzaType = scanner.next();
            if (SH.equals(pizzaType)) {
                new OrderPizza(new ShPizzaFactory());
            } else if (BJ.equals(pizzaType)) {
                new OrderPizza(new BjPizzaFactory());
            } else {
                System.out.println("本店暂未提供该地区披萨！");
            }
        } while (true);
    }
}

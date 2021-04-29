package com.xlj.tools.patterns.factory.method;


import java.util.Scanner;


/**
 * 地区披萨主方法
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
                new ShPizzaFactory();
            }
            else if (BJ.equals(pizzaType)) {
                new BjPizzaFactory();
            }
            else {
                System.out.println("本店暂未提供该地区披萨！");
            }
        } while (true);
    }
}

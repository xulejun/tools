package com.xlj.tools.basis.patterns.factory.method;

import com.xlj.tools.basis.patterns.factory.simple.Pizza;

import java.util.Scanner;

/**
 * 订购披萨
 *
 * @author xlj
 * @date 2020/12/15 21:44
 */
public abstract class OrderPizza {

    /**
     * 创建披萨
     * @param pizzaType
     * @return
     */
    public abstract Pizza createPizza(String pizzaType);

    public OrderPizza() {
        System.out.println("请输入披萨口味：");
        Scanner scanner = new Scanner(System.in);
        String pizzaType = scanner.next();
        Pizza pizza = createPizza(pizzaType);
        if (pizza != null) {
            pizza.prepare();
            pizza.bake();
            pizza.cut();
            pizza.box();
        } else {
            System.out.println("该店未有此款披萨");
        }
    }
}

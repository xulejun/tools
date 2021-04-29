package com.xlj.tools.basis.patterns.factory.simple;

import java.util.Scanner;

import static com.xlj.tools.basis.patterns.factory.simple.PizzaSimpleFactory.createPizza2;

/**
 * @author xlj
 * @date 2020/12/14 23:17
 */
public class OrderPizza {

    public static void main(String[] args) {
        orderPizza();
    }

    private static void orderPizza() {
        PizzaSimpleFactory pizzaSimpleFactory = new PizzaSimpleFactory();
        Scanner scanner = new Scanner(System.in);
        do {
            String pizzaType = scanner.next();
            Pizza pizza = pizzaSimpleFactory.createPizza(pizzaType);
            // 使用静态工厂模式
            Pizza pizza2 = createPizza2(pizzaType);
            if (pizza != null) {
                pizza.prepare();
                pizza.bake();
                pizza.cut();
                pizza.box();
            } else {
                System.out.println("该店未有此款披萨");
                break;
            }
        } while (true);
    }
}

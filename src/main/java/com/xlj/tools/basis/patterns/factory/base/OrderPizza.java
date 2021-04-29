package com.xlj.tools.basis.patterns.factory.base;


import com.xlj.tools.basis.patterns.factory.simple.Pizza;

import java.util.Scanner;

/**
 * 订购披萨
 *
 * @author xlj
 * @date 2020/12/15 21:44
 */
public class OrderPizza {

    AbstractFactoryPizza abstractFactoryPizza;

    public OrderPizza(AbstractFactoryPizza abstractFactoryPizza){
        setAbstractFactoryPizza(abstractFactoryPizza);
    }

    public void setAbstractFactoryPizza(AbstractFactoryPizza abstractFactoryPizza) {
        this.abstractFactoryPizza = abstractFactoryPizza;
        System.out.println("请输入披萨口味：");
        Scanner scanner = new Scanner(System.in);
        String pizzaType = scanner.next();
        Pizza pizza = abstractFactoryPizza.createPizza(pizzaType);
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

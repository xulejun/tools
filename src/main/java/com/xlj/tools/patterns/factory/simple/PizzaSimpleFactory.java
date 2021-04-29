package com.xlj.tools.patterns.factory.simple;

/**
 * 简单工厂
 *
 * @author xlj
 * @date 2020/12/14 23:06
 */
public class PizzaSimpleFactory {
    private static final String CHEESE = "cheese";
    private static final String DURIAN = "durian";

    public Pizza createPizza(String pizzaType){
        Pizza pizza = null;
        if (CHEESE.equals(pizzaType)){
            pizza = new CheesePizza();
        }
        if (DURIAN.equals(pizzaType)){
            pizza = new DurianPizza();
        }
        return pizza;
    }

    /**
    * @description 简单工厂模式也叫静态工厂模式
    * @author xlj
    * @date 2020/12/15 21:09
    */
    public static Pizza createPizza2(String pizzaType){
        Pizza pizza = null;
        if (CHEESE.equals(pizzaType)){
            pizza = new CheesePizza();
        }
        if (DURIAN.equals(pizzaType)){
            pizza = new DurianPizza();
        }
        return pizza;
    }
}

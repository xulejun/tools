package com.xlj.tools.patterns.factory.base;


import com.xlj.tools.patterns.factory.method.ShCheesePizza;
import com.xlj.tools.patterns.factory.method.ShDurianPizza;
import com.xlj.tools.patterns.factory.simple.Pizza;

/**
 * 抽象工厂模式：上海披萨
 *
 * @author xlj
 * @date 2020/12/15 21:59
 */
public class ShPizzaFactory implements AbstractFactoryPizza {
    private static final String CHEESE = "cheese";
    private static final String DURIAN = "durian";

    @Override
    public Pizza createPizza(String pizzaType) {
        Pizza pizza = null;
        if (CHEESE.equals(pizzaType)) {
            pizza = new ShCheesePizza();
        }
        if (DURIAN.equals(pizzaType)) {
            pizza = new ShDurianPizza();
        }
        return pizza;
    }
}

package com.xlj.tools.basis.patterns.factory.base;


import com.xlj.tools.basis.patterns.factory.method.BjCheesePizza;
import com.xlj.tools.basis.patterns.factory.method.BjDurianPizza;
import com.xlj.tools.basis.patterns.factory.simple.Pizza;

/**
 * 抽象工厂模式：北京披萨
 *
 * @author xlj
 * @date 2020/12/15 21:59
 */
public class BjPizzaFactory implements AbstractFactoryPizza {
    private static final String CHEESE = "cheese";
    private static final String DURIAN = "durian";

    @Override
    public Pizza createPizza(String pizzaType) {
        Pizza pizza = null;
        if (CHEESE.equals(pizzaType)) {
            pizza = new BjCheesePizza();
        }
        if (DURIAN.equals(pizzaType)) {
            pizza = new BjDurianPizza();
        }
        return pizza;
    }
}

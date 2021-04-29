package com.xlj.tools.basis.patterns.factory.base;


import com.xlj.tools.basis.patterns.factory.simple.Pizza;

/**
 * 抽象工厂模式：抽象工厂类
 *
 * @author xlj
 * @date 2020/12/16 21:36
 */
public interface AbstractFactoryPizza {
    /**
     * 创建pizza
     *
     * @param orderPizza
     * @return
     */
    Pizza createPizza(String orderPizza);
}

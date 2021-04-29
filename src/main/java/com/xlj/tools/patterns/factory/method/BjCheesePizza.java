package com.xlj.tools.patterns.factory.method;

import com.xlj.tools.patterns.factory.simple.Pizza;

/**
 * @author xlj
 * @date 2020/12/15 21:41
 */
public class BjCheesePizza extends Pizza {
    @Override
    public void prepare() {
        this.setName("北京奶酪披萨");
        System.out.println("北京奶酪披萨准备原材料");
    }
}

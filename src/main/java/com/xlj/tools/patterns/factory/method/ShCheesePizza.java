package com.xlj.tools.patterns.factory.method;


import com.xlj.tools.patterns.factory.simple.Pizza;

/**
 * @author xlj
 * @date 2020/12/15 21:41
 */
public class ShCheesePizza extends Pizza {
    @Override
    public void prepare() {
        this.setName("上海奶酪披萨");
        System.out.println("上海奶酪披萨准备原材料");
    }
}

package com.xlj.tools.patterns.factory.simple;

/**
 * 奶酪披萨
 *
 * @author xlj
 * @date 2020/12/14 23:03
 */
public class CheesePizza extends Pizza {
    public CheesePizza() {
        this.setName("奶酪披萨");
    }

    @Override
    public void prepare() {
        System.out.println("奶酪披萨准备原材料");
    }

}

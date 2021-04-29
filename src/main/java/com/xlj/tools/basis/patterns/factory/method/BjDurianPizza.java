package com.xlj.tools.basis.patterns.factory.method;

import com.xlj.tools.basis.patterns.factory.simple.Pizza;

/**
 * @author xlj
 * @date 2020/12/15 21:41
 */
public class BjDurianPizza extends Pizza {
    @Override
    public void prepare() {
        this.setName("北京榴莲披萨");
        System.out.println("北京榴莲披萨准备原材料");
    }
}

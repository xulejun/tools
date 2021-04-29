package com.xlj.tools.basis.patterns.factory.method;

import com.xlj.tools.basis.patterns.factory.simple.Pizza;

/**
 * @author xlj
 * @date 2020/12/15 21:41
 */
public class ShDurianPizza extends Pizza {
    @Override
    public void prepare() {
        this.setName("上海榴莲披萨");
        System.out.println("上海榴莲披萨准备原材料");
    }
}

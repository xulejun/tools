package com.xlj.tools.basis.patterns.factory.simple;

/**
 * 榴莲披萨
 *
 * @author xlj
 * @date 2020/12/14 23:04
 */
public class DurianPizza extends Pizza {
    public DurianPizza() {
        this.setName("榴莲披萨");
    }

    @Override
    public void prepare() {
        System.out.println("榴莲披萨准备原材料");
    }
}

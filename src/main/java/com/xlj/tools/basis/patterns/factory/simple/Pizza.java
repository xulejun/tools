package com.xlj.tools.basis.patterns.factory.simple;

/**
 * 披萨-抽象类
 *
 * @author xlj
 * @date 2020/12/14 22:57
 */
public abstract class Pizza {
    public String name;

    /**
     * 原材料
     */
    public abstract void prepare();

    public void bake() {
        System.out.println(name + "正在烘烤");
    }

    public void cut() {
        System.out.println(name + "正在切片");
    }

    public void box() {
        System.out.println(name + "正在打包");
    }

    public void setName(String name) {
        this.name = name;
    }
}

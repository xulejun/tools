package com.xlj.tools.basis.patterns.factory.build;

/**
 * 高楼实现类
 *
 * @author xlj
 * @date 2020/12/16 23:13
 */
public class HightHouse extends HouseBuild {
    @Override
    public void buildGround() {
        System.out.println("高楼打地基100m");
    }

    @Override
    public void buildWall() {
        System.out.println("高楼砌墙10m");
    }

    @Override
    public void buildRoofed() {
        System.out.println("高楼盖房顶");
    }
}

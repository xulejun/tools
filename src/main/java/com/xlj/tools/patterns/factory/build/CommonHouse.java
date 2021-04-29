package com.xlj.tools.patterns.factory.build;

/**
 * 普通房子实现类
 *
 * @author xlj
 * @date 2020/12/16 23:13
 */
public class CommonHouse extends HouseBuild {
    @Override
    public void buildGround() {
        System.out.println("普通房子打地基10m");
    }

    @Override
    public void buildWall() {
        System.out.println("普通房子砌墙2m");
    }

    @Override
    public void buildRoofed() {
        System.out.println("普通房子盖房顶");
    }
}

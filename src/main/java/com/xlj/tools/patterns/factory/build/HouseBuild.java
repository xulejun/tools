package com.xlj.tools.patterns.factory.build;

/**
 * 建造者模式-建造者
 *
 * @author xlj
 * @date 2020/12/16 23:05
 */
public abstract class HouseBuild {
    public House house = new House();

    /**
     * 打地基
     */
    public abstract void buildGround();

    /**
     * 砌墙
     */
    public abstract void buildWall();

    /**
     * 屋顶
     */
    public abstract void buildRoofed();

    /**
     * 房子构建
     * @return
     */
    public House buildHouse(){
        return house;
    }


}

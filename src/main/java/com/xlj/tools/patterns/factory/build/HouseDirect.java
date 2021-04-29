package com.xlj.tools.patterns.factory.build;

/**
 * 建造者模式-指挥者
 *
 * @author xlj
 * @date 2020/12/16 23:16
 */
public class HouseDirect {
    HouseBuild houseBuild = null;

    /**
     * 通过构造方法传入对象
     * @param houseBuild
     */
    public HouseDirect(HouseBuild houseBuild){
        this.houseBuild = houseBuild;
    }

    /**
     * 通过set方法传入对象
     * @param houseBuild
     */
    public void setHouseBuild(HouseBuild houseBuild) {
        this.houseBuild = houseBuild;
    }

    /**
     * 构建房子
     * @return
     */
    public House createHouse(){
        houseBuild.buildGround();
        houseBuild.buildWall();
        houseBuild.buildRoofed();
        return houseBuild.buildHouse();
    }
}

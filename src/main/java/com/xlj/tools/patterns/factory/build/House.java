package com.xlj.tools.patterns.factory.build;

/**
 * 建造者模式：产品
 *
 * @author xlj
 * @date 2020/12/16 23:01
 */
public class House {
    /**
     * 地基
     */
    private String ground;

    /**
     * 砌墙
     */
    private String wall;

    /**
     * 屋顶
     */
    private String roofed;

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    public String getRoofed() {
        return roofed;
    }

    public void setRoofed(String roofed) {
        this.roofed = roofed;
    }
}

package com.xlj.tools.bean;

/**
 * @author lejunxu
 */
public class Task {
    private int type;
    private long buildTime;
    private int id;
    private int priority;
    private QuaOcrHotelStartingPriceTaskDetail taskDetail;
    private String proxy="proxy";
    private String level = "P0";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getInterval() {
        return priority;
    }

    public void setInterval(int interval) {
        this.priority = interval;
    }

    public QuaOcrHotelStartingPriceTaskDetail getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(QuaOcrHotelStartingPriceTaskDetail taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Task{" +
                "type=" + type +
                ", buildTime=" + buildTime +
                ", id=" + id +
                ", priority=" + priority +
                ", taskDetail=" + taskDetail +
                ", proxy='" + proxy + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}

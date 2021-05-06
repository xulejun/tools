package com.xlj.tools.enums;

/**
 * @author xlj
 * @date 2021/3/12
 */
public enum InfoTypeEnum {
    /**
     * 文章
     */
    ARTICLE("文章", 1),
    /**
     * 招标
     */
    ZHAO_BIAO_GONG_GAO("招标公告", 2),
    /**
     * 中标
     */
    ZHONG_BIAO_GONG_GAO("中标公告", 3);
    /**
     * 描述
     */
    private String desc;
    /**
     * 枚举值
     */
    private Integer val;

    InfoTypeEnum(String desc, int val) {
        this.desc = desc;
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getVal() {
        return val;
    }
}

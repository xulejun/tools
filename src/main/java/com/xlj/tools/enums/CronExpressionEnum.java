package com.xlj.tools.enums;

import lombok.Getter;

/**
 * @author xlj
 * @date 2021/3/9
 */
@Getter
public enum CronExpressionEnum {
    /**
     * 1分钟
     */
    ONE_MINUTE("0 0/1 * * * ?","1分钟"),

    /**
     * 5分钟
     */
    FIVE_MINUTE("0 0/5 * * * ?","5分钟"),
    /**
     * 10分钟
     */
    TEN_MINUTE("0 0/10 * * * ?","10分钟"),
    /**
     * 15分钟
     */
    FIFTEEN_MINUTE("0 0/15 * * * ?","15分钟"),
    /**
     * 30分钟
     */
    THIRTY_MINUTE("0 0/30 * * * ?","30分钟"),
    /**
     * 1小时
     */
    ONE_HOUR("0 0 0/1 * * ?","1小时"),
    /**
     * 1天
     */
    ONE_DAY("0 0 0 1/1 * ?","1天"),
    /**
     * 1周
     */
    ONE_WEEK("0 0 0 ? * 1","1周"),
    /**
     * 1月
     */
    ONE_MONTH("0 0 0 1 1 ?","1月"),
    ;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 采集频率
     */
    private String frequency;

    CronExpressionEnum(String cronExpression, String frequency) {
        this.cronExpression = cronExpression;
        this.frequency = frequency;
    }
}

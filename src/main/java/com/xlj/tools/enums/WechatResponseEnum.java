package com.xlj.tools.enums;

/**
 * 微信采集响应状态码
 *
 * @author xlj
 * @date 2021/4/26
 */
public enum WechatResponseEnum {
    /**
     * 获取fakeId成功
     */
    SUCCESS_CODE("0", "获取fakeId成功"),
    /**
     * 公众号id有误
     */
    SYSTEM_ERROR("-1", "公众号id有误"),
    /**
     * 账号已被限流
     */
    FREQ_CONTROL("200013", "账号已被限流"),
    /**
     * cookie已失效
     */
    INVALID_SESSION("200003", "cookie已失效");

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    WechatResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

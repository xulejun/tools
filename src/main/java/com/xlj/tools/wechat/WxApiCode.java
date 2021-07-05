package com.xlj.tools.wechat;

/**
 * @author xlj
 * @date 2021/4/26
 */
public final class WxApiCode {

    private WxApiCode() {
    }

    /**
     * 获取fakeId成功
     */
    public static final String SUCCESS_CODE = "0";
    /**
     * 公众号id有误
     */
    public static final String SYSTEM_ERROR = "-1";
    /**
     * 账号限流
     */
    public static final String FREQ_CONTROL = "200013";
}

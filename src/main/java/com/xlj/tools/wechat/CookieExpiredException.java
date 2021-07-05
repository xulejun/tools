package com.xlj.tools.wechat;

/***
 * @description: cookies失效
 * @author XLJ
 * @date 2021/4/22 15:36
 */
public class CookieExpiredException extends RuntimeException {
    public CookieExpiredException() {
    }

    public CookieExpiredException(String message) {
        super(message);
    }
}

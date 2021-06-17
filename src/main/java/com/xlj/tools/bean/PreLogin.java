package com.xlj.tools.bean;

import lombok.Data;

/**
 * 预登陆配置
 *
 * @author xlj
 * @date 2021/6/15
 */
@Data
public class PreLogin {
    /**
     * 用户名Xpath
     */
    private String userNameXpath;
    /**
     * 密码Xpath
     */
    private String passwordXpath;
    /**
     * 登录Xpath
     */
    private String loginXpath;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 登录url
     */
    private String loginUrl;
    /**
     * 文章url
     */
    private String articleUrl;

}

package com.xlj.tools.wechat;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.xlj.tools.enums.WechatResponseEnum.*;

/**
 * @author xlj
 * @date 2021/4/9
 */
@Slf4j
public class WechatLogin {
    /**
     * 微信公众号登陆URL
     */
    public static final String LOGINURL = "https://mp.weixin.qq.com/";
    /**
     * 公众号搜索
     */
    public static final String SEARCHURL = "https://mp.weixin.qq.com/cgi-bin/searchbiz?action=search_biz&begin=0&count=5&query=%s&lang=zh_CN&f=json&ajax=1&token=%s";
    /**
     * 来源
     */
    public static final String REFERER = "https://mp.weixin.qq.com/cgi-bin/appmsg?t=media/appmsg_edit_v2&action=edit&isNew=1&type=10&lang=zh_CN&token=%s";
    /**
     * 请求头：HOST
     */
    public static final String HOST = "mp.weixin.qq.com";
    /**
     * 请求头：User-Agent
     */
    public static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0";
    /**
     * 访问列表页地址
     */
    public static final String APPMSGURL = "https://mp.weixin.qq.com/cgi-bin/appmsg?action=list_ex&begin=%d&count=5&fakeid=%s&type=9&query=&token=%s&lang=zh_CN&f=json&ajax=1";

    /**
     * 通过cookie进行登录重定向，获取token
     *
     * @param cookies
     * @return
     */
    public static String getToken(String cookies) throws Exception {
        String location;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpClientContext context = HttpClientContext.create();

            HttpGet httpGet = new HttpGet(LOGINURL);
            httpGet.addHeader("Cookie", cookies);
            // 设置超时时间 15s
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(15000).setConnectionRequestTimeout(15000).setSocketTimeout(15000).build();
            httpGet.setConfig(requestConfig);

            try (CloseableHttpResponse response = httpClient.execute(httpGet, context)) {
                HttpHost target = context.getTargetHost();
                List<URI> redirectLocations = context.getRedirectLocations();
                // 通过cookie 登录重定向（https://mp.weixin.qq.com/cgi-bin/home?t=home/index&lang=zh_CN&token=1193432834）
                location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations).toASCIIString();
            }
        }
        return location.substring(location.indexOf("token=") + 6);
    }

    /**
     * 获取当前公众号fakeId
     *
     * @param cookies
     * @param query   公众号
     * @param token
     * @return
     * @throws IOException
     */
    public static String getFakeId(String cookies, String query, String token) {
        final String result = HttpRequest.get(String.format(SEARCHURL, query, token))
                .header(Header.REFERER, String.format(REFERER, token))
                .header(Header.COOKIE, cookies)
                .header(Header.HOST, HOST)
                .header(Header.USER_AGENT, USERAGENT)
                .timeout(15000).execute().body();
        JSONObject resultJson = JSONUtil.parseObj(result);
        String responseStatus = resultJson.getByPath("base_resp.ret", String.class);
        log.info("微信公众号响应信息：{}", resultJson.getByPath("base_resp", String.class));
        if (SUCCESS_CODE.getCode().equals(responseStatus)) {
            return resultJson.getByPath("list[0].fakeid", String.class);
        } else if (SYSTEM_ERROR.getCode().equals(responseStatus)) {
            log.warn("公众号有误，请核对所采集的公众号");
            return SYSTEM_ERROR.getCode();
        } else if (INVALID_SESSION.getCode().equals(responseStatus)) {
            log.warn("cookie 失效，请重新获取cookie");
            return INVALID_SESSION.getCode();
        } else {
            throw new CookieExpiredException("result=" + result + "\n，cookie=" + cookies);
        }
    }
}

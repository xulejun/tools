package com.xlj.tools.util;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

/**
 * 通过代理请求外部接口工具类
 *
 * @author legend xu
 * @date 2021/11/9
 */
@Slf4j
@Component
public class ProxyUtil {
    private RestTemplate restTemplate;

    /**
     * 获取代理并封装到restTemplate
     */
    @PostConstruct
    public void init() {
        log.info("---------------加载电子身份证服务接口配置---------------");
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // 配置代理服务器
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 9000));
        simpleClientHttpRequestFactory.setProxy(proxy);
        restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
    }

    /**
     * 传参调用外部接口
     *
     * @param url
     * @param param
     * @return
     */
    public Object call(String url, Map<String, String> param) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        param.put("username", "username");
        param.put("token", "token");
        String requestBody = new JSONObject(param).toString();
        HttpEntity httpEntity = new HttpEntity(requestBody, httpHeaders);
        String response = restTemplate.postForObject(url, httpEntity, String.class);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }
}

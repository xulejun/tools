package com.xlj.tools.util;

import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * okhttp 的使用
 *
 * @author legend xu
 * @date 2023/3/23
 */
public class OkHttpUtil {
    private static final int TIMEOUT = 15;
    /**
     * 默认创建连接池最多可容纳5个空闲连接，这些连接将在不活动5分钟后被收回
     */
    private static final OkHttpClient OKHTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    /**
     * get 请求携带 Map 类型的 查询参数
     *
     * @param url
     * @param queryParams
     * @throws IOException
     */
    public static ResponseBody get(String url, Map<String, String> queryParams) throws IOException {
        HttpUrl.Builder builder = HttpUrl.get(url).newBuilder();
        queryParams.forEach(builder::setQueryParameter);

        Request request = new Request.Builder().url(builder.build()).get().build();

        // 携带请求头的完整 url
        // Request request = new Request.Builder().url(url).addHeader("Auth","xxx").build();

        ResponseBody body = OKHTTP_CLIENT.newCall(request).execute().body();
        return body;
    }

    /**
     * post 请求携带 请求体
     *
     * @param url
     * @param queryParams
     * @throws IOException
     */
    public static ResponseBody post(String url, Map<String, String> queryParams, String requestBody) throws IOException {
        // HttpServletRequest 获取请求体
        // String data = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        HttpUrl.Builder builder = HttpUrl.get(url).newBuilder();
        queryParams.forEach(builder::setQueryParameter);

        Request request = new Request.Builder().url(builder.build()).post(RequestBody.create(MediaType.parse("application/json"), requestBody)).build();

        // 简单的 post 请求， 不携带任何请求体
        // Request request = new Request.Builder().url(url).post(new FormBody.Builder().build()).build();

        return OKHTTP_CLIENT.newCall(request).execute().body();
    }

    public static void main(String[] args) throws Exception {
        String url = "http://vn.cc.ctripcorp.com/vnumber-ui/soa/vnumber/smsQueryByOneNx";
        String requestBody = "";
        ResponseBody body = post(url, new HashMap<>(), requestBody);
        System.out.println(body.string());
    }

}

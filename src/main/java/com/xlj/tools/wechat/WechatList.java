package com.xlj.tools.wechat;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.xlj.tools.enums.WechatResponseEnum.*;
import static com.xlj.tools.wechat.WechatLogin.*;

/**
 * @author xlj
 * @date 2021/4/14
 */
@Slf4j
public class WechatList {
    /**
     * 获取公众号文章列表页信息
     *
     * @param token
     * @param fakeId
     * @param totalNum
     * @return
     */
    public static List<WechatArticleBean> getWeixinArticleList(String token, String fakeId, int totalNum) throws Exception {
        // URL翻页参数
        int index = 5;
        // 循环次数
        int times = totalNum / index;
        // 页数取余
        int remainder = totalNum % index;
        // 页数
        int countTime = 1;
        // 访问列表参数
        int pos = 0;
        Random random = new Random();
        // 目前只采集首页，不需要循环，后续需要翻页采集再开启
//        while (times > 0 && countTime < 2) {
//        log.info("开始爬取第{}页", countTime);
        // 采集地址列表页
        String listUrl = String.format(APPMSGURL, pos, fakeId, token);
        // 设置列表页，在日志推送需要用到
//        spiderWxJob.setUrl(listUrl);
//        pos = countTime * index;

        String refererUrl = String.format(REFERER, token);
        final String result = HttpRequest.get(listUrl)
//                .header(Header.COOKIE, spiderWxJob.getCookie())
                .header(Header.REFERER, refererUrl)
                .header(Header.USER_AGENT, USERAGENT)
                .timeout(15 * 1000)
                .execute().body();
        JSONObject resultJson = JSONUtil.parseObj(result);
        String responseStatus = resultJson.getByPath("base_resp.ret", String.class);
        if (FREQ_CONTROL.getCode().equals(responseStatus)) {
            log.warn(FREQ_CONTROL.getMsg());
            return null;
        }
        JSONArray jsonArray = resultJson.getByPath("app_msg_list", JSONArray.class);
        if (CollUtil.isEmpty(jsonArray)) {
            return Collections.emptyList();
        }
        // 公众号列表页字段数据组装
        return wechatArticleList(jsonArray);
    }

    private static List<WechatArticleBean> wechatArticleList(JSONArray jsonArray) {
        List<WechatArticleBean> list = new ArrayList<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            WechatArticleBean wechatArticle = WechatArticleBean.builder()
                    .link(job.getStr("link"))
                    .title(job.getStr("title"))
                    // 从公众号列表下获取到的时间是秒
                    .articleTime(job.getLong("update_time") * 1000).build();
            list.add(wechatArticle);
        }
        return list;
    }
}

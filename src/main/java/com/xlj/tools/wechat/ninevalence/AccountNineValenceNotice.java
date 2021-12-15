package com.xlj.tools.wechat.ninevalence;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.xlj.tools.util.MailUtil;
import com.xlj.tools.wechat.CookieExpiredException;
import com.xlj.tools.wechat.WechatArticleBean;
import com.xlj.tools.wechat.WechatLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xlj.tools.enums.WechatResponseEnum.*;
import static com.xlj.tools.wechat.WechatLogin.*;

/**
 * 微信公众号九价文章采集通知
 *
 * @author legend xu
 * @date 2021/11/25
 */
@Slf4j
@Component
public class AccountNineValenceNotice {
    @Value("${wechat.cookie}")
    private String cookie;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Value("${mail.addressee}")
    private String sendTo;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * hashCookie 相关字段
     */
    private static final String HASH_WECHAT_COOKIE = "wechat.cookie.%s";
    static final String COOKIE_HK = "cookie";
    static final String CRAWL_COUNT_HK = "crawlCount";
    static final String LIMIT_CRAWL_COUNT_HK = "limitCrawlCount";
    static final String CREATE_TIME_HK = "createTime";
    static final String LIMIT_TIME_HK = "limitTime";
    static final String INVALID_TIME_HK = "invalidTime";

    private static List<String> queryAccountList = Lists.newArrayList();

    int invalidCount = 1;

    static {
        queryAccountList.add("公园接种");
        queryAccountList.add("董家窑接种");
        queryAccountList.add("滕王阁街道社区卫生服务中心");
//        queryAccountList.add("南昌市岱山街道社区卫生服务中心");
//        queryAccountList.add("蛟桥镇医院");
//        queryAccountList.add("高新艾溪湖社区服务中心");
    }

    public void notice() throws InterruptedException {
        String cookieKey = String.format(HASH_WECHAT_COOKIE, cookie.substring(cookie.indexOf("uuid")));
        JSONArray articleJsonArray = null;
        for (String queryAccount : queryAccountList) {
            String responseStatus;
            try {
                // cookie记录
                cookieRecord(cookieKey);
                // 登录信息
                String token = WechatLogin.getToken(cookie);
                String fakeId = WechatLogin.getFakeId(cookie, queryAccount, token);
                // 访问列表参数
                int pos = 0;
                // 采集地址列表页
                String listUrl = String.format(APPMSGURL, pos, fakeId, token);
                // 请求referer
                String refererUrl = String.format(REFERER, token);
                String result = HttpRequest.get(listUrl)
                        .header(Header.REFERER, refererUrl)
                        .header(Header.USER_AGENT, USERAGENT)
                        .timeout(15 * 1000)
                        .execute().body();
                JSONObject resultJson = JSONUtil.parseObj(result);
                responseStatus = resultJson.getByPath("base_resp.ret", String.class);
                // 结果校验
                if (resultCheck(cookieKey, responseStatus)) {
                    return;
                }
                articleJsonArray = resultJson.getByPath("app_msg_list", JSONArray.class);
                if (CollUtil.isEmpty(articleJsonArray)) {
                    log.warn("公众号={}，文章采集列表为空，{}", queryAccount, resultJson);
                    continue;
                }
            } catch (CookieExpiredException e) {
                if (!resultCheck(cookieKey, new JSONObject(e.getMessage()).getByPath("base_resp.ret").toString())) {
                    log.warn("获取公众号fakeId异常：{}", e.getMessage());
                }
                continue;
            } catch (Exception e) {
                log.warn("采集文章失败，queryAccount={}", queryAccount, e);
                continue;
            }
            // 数据处理
            dealData(articleJsonArray);
            // 记录采集次数
            redisTemplate.opsForHash().increment(cookieKey, CRAWL_COUNT_HK, 1L);
            // 随机睡眠，保证账号安全
            TimeUnit.SECONDS.sleep(RandomUtil.randomLong(10, 30));
        }
    }

    private void dealData(JSONArray articleJsonArray) {
        JSONObject article = articleJsonArray.getJSONObject(0);
        String title = article.getStr("title");
        String articleUrl = article.getStr("link");
        long articleTime = article.getLong("update_time") * 1000;
        log.info("文章标题={}，文章链接={}", title, articleUrl);
        // 文章标题 含有 九价 ，并且 redis 中不存在数据
        String setNineValenceArticleUrlKey = "nineValence.articleUrl";
        boolean isNeed = title.contains("九价") && (!redisTemplate.opsForSet().isMember(setNineValenceArticleUrlKey, articleUrl));

        // 邮件发送
        if (isNeed) {
            String[] addressee = sendTo.split(",");
            String content = "<h1 style=\"margin-top: 60%;text-align: center\"><a href=\"" + articleUrl + "\">点击此处查看文章内容</a></h1>";
            mailUtil.sendHtmlMail(sendFrom, addressee, title, content);

            // 数据入库-redis
            redisTemplate.opsForSet().add(setNineValenceArticleUrlKey, articleUrl);
            WechatArticleBean articleBean = WechatArticleBean.builder().title(title).link(articleUrl).articleTime(articleTime).build();
            String articleJsonStr = JSONUtil.toJsonStr(articleBean);
            String setNineValenceArticleKey = "nineValence.article";
            redisTemplate.opsForSet().add(setNineValenceArticleKey, articleJsonStr);
        }
    }

    public boolean resultCheck(String cookieKey, String response) {
        if (FREQ_CONTROL.getCode().equals(response)) {
            log.warn(FREQ_CONTROL.getMsg());
            redisTemplate.opsForHash().putIfAbsent(cookieKey, LIMIT_TIME_HK, DateUtil.now());
            redisTemplate.opsForHash().putIfAbsent(cookieKey, LIMIT_CRAWL_COUNT_HK, redisTemplate.opsForHash().get(cookieKey, CRAWL_COUNT_HK));
            return true;
        } else if (INVALID_SESSION.getCode().equals(response)) {
            log.warn(INVALID_SESSION.getMsg());
            // cookie失效只发一次邮件
            if (invalidCount <= 1) {
                String[] addressee = {"xu-lejun@qq.com"};
                mailUtil.sendHtmlMail(sendFrom, addressee, "微信公众号cookie失效", "请及时添加cookie");
            }
            // 记录cookie失效时间
            redisTemplate.opsForHash().put(cookieKey, INVALID_TIME_HK, DateUtil.now());
            invalidCount++;
            return true;
        }
        return false;
    }

    private void cookieRecord(String cookieKey) {
        // cookie状态记录
        if (!redisTemplate.hasKey(cookieKey)) {
            redisTemplate.opsForHash().put(cookieKey, COOKIE_HK, cookie);
            redisTemplate.opsForHash().put(cookieKey, CREATE_TIME_HK, DateUtil.now());
        }
    }
}

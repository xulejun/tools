package com.xlj.tools.wechat.ninevalence;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.xlj.tools.wechat.WechatArticleBean;
import com.xlj.tools.wechat.WechatLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xlj.tools.wechat.WechatLogin.*;
import static com.xlj.tools.wechat.WxApiCode.FREQ_CONTROL;

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
    private String sendName;

    @Value("${mail.addressee}")
    private String addresseeStr;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    private static List<String> queryAccountList = Lists.newArrayList();

    static {
        queryAccountList.add("公园接种");
        queryAccountList.add("董家窑接种");
        queryAccountList.add("滕王阁街道社区卫生服务中心");
//        queryAccountList.add("南昌市岱山街道社区卫生服务中心");
//        queryAccountList.add("蛟桥镇医院");
//        queryAccountList.add("高新艾溪湖社区服务中心");
    }

    public void notice() throws InterruptedException {
        JSONArray articleJsonArray = null;

        for (String queryAccount : queryAccountList) {
            String responseStatus;
            try {
                // 登录信息
                String token = WechatLogin.getToken(cookie);
                String fakeId = WechatLogin.getFakeId(cookie, queryAccount, token);
                if (StrUtil.isBlank(fakeId)) {
                    return;
                }
                // 采集文章
                int pos = 0;    // 访问列表参数
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

                // 响应结果校验
                responseStatus = resultJson.getByPath("base_resp.ret", String.class);

                if (FREQ_CONTROL.equals(responseStatus)) {
                    log.warn("账号已经被限流");
                    return;
                }
                articleJsonArray = resultJson.getByPath("app_msg_list", JSONArray.class);
                if (CollUtil.isEmpty(articleJsonArray)) {
                    log.warn("文章采集列表为空，{}", articleJsonArray);
                    return;
                }
            } catch (Exception e) {
                log.warn("采集文章失败，queryAccount={}", queryAccount, e);
                return;
            }
            // 数据处理
            dealData(articleJsonArray, responseStatus);
            // 随机睡眠，保证账号安全
            TimeUnit.SECONDS.sleep(RandomUtil.randomLong(10, 30));
        }
    }

    private void dealData(JSONArray articleJsonArray, String responseStatus) {
        JSONObject article = articleJsonArray.getJSONObject(0);
        String title = article.getStr("title");
        String articleUrl = article.getStr("link");
        long articleTime = article.getLong("update_time") * 1000;
        String setNineValenceArticleUrlKey = "nineValence.articleUrl";
        log.info("响应状态码={}，文章标题={}，文章链接={}", responseStatus, title, articleUrl);
        // 文章标题 含有 九价 ，并且 redis 中不存在数据
        boolean isNeed = title.contains("九价") && (!redisTemplate.opsForSet().isMember(setNineValenceArticleUrlKey, articleUrl));

        // 邮件发送
        if (isNeed) {
            String[] addressee = addresseeStr.split(",");
            String content = "<h1 style=\"margin-top: 60%;text-align: center\"><a href=\"" + articleUrl + "\">点击此处查看文章内容</a></h1>";
            sendHtmlMail(title, addressee, content);

            // 数据入库-redis
            redisTemplate.opsForSet().add(setNineValenceArticleUrlKey, articleUrl);
            WechatArticleBean articleBean = WechatArticleBean.builder().title(title).link(articleUrl).articleTime(articleTime).build();
            String setNineValenceArticleKey = "nineValence.article";
            String articleJsonStr = JSONUtil.toJsonStr(articleBean);
            redisTemplate.opsForSet().add(setNineValenceArticleKey, articleJsonStr);
        }
    }

    /**
     * 发送htmlMail
     *
     * @param title
     * @param addressee
     * @param content
     */
    public void sendHtmlMail(String title, String[] addressee, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(sendName);
            messageHelper.setTo(addressee);
            messageHelper.setSubject(title);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.warn("邮件发送失败:", e);
        }
    }
}

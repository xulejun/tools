package com.xlj.tools.util;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URL;
import java.util.*;


/**
 * httpClient 模拟登录
 *
 * @author xlj
 * @date 2021/5/18
 */
@Slf4j
public class HttpLoginUtil {

    public static void main(String[] args) throws Exception {
        jsPreLogin();

//        zcPreLogin();
//        lgPreLogin();
    }

    /**
     * 简数——预登陆
     *
     * @throws java.io.IOException
     */
    private static void jsPreLogin() throws Exception {
        // webRequest请求参数
        String loginUrl = "http://account.keydatas.com/login";
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
        List<NameValuePair> param = new ArrayList<>();
        param.add(new NameValuePair("account", "xule_jun@163.com"));
        param.add(new NameValuePair("password", "xulejun520"));
        param.add(new NameValuePair("source", "http://www.keydatas.com/product"));
        param.add(new NameValuePair("rememberMe", "false"));
        webRequest.setRequestParameters(param);

        // 添加请求头访问
        WebClient webClient = getWebClient();
        webClient.addRequestHeader("origin", "http://www.keydatas.com");
        webClient.addRequestHeader("Host", "account.keydatas.com");
        webClient.addRequestHeader("Referer", "http://www.keydatas.com/");
        webClient.getPage(webRequest).getWebResponse();

        // 获取cookies
        StringBuilder cookies = new StringBuilder();
        webClient.getCookieManager().getCookies().forEach(cookie -> {
            cookies.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        });
        log.info("获取到的cookies：{}", cookies);
        // 获取登录后的文章内容
//        webClient.addRequestHeader("origin", "http://dash.keydatas.com");
//        webClient.addRequestHeader("Host", "dash.keydatas.com");
//        webClient.addRequestHeader("Referer", "http://dash.keydatas.com/task/list?newTask=wizard");
//        String articleUrl = "http://dash.keydatas.com/task/group/list";
//        WebRequest webRequest1 = new WebRequest(new URL(articleUrl), HttpMethod.POST);
//        String content = webClient.getPage(webRequest1).getWebResponse().getContentAsString();
//        log.info("请求内容：{}，\n 是否含有文章内容：{}", content, content.contains("任务分组"));
    }

    /**
     * 兰格资讯——预登陆
     *
     * @throws Exception
     */
    private static void lgPreLogin() throws Exception {
        // webRequest请求参数
        String loginUrl = "https://login.lgmi.com/clientlogin.aspx";
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
        List<NameValuePair> param = new ArrayList<>();
        param.add(new NameValuePair("ClientID", "badboy"));
        param.add(new NameValuePair("ClientPassword", "xulejun520."));
        param.add(new NameValuePair("submit", "%B5%C7%C2%BC"));
        webRequest.setRequestParameters(param);

        // 添加请求头访问
        WebClient webClient = getWebClient();
        webClient.addRequestHeader("origin", "https://bancai.lgmi.com");
        webClient.addRequestHeader("Host", "login.lgmi.com");
        webClient.addRequestHeader("Referer", "https://bancai.lgmi.com/");
        webClient.getPage(webRequest).getWebResponse();

        // 获取登录后的文章内容
        webClient.addRequestHeader("Host", "jiancai.lgmi.com");
        String articleUrl = "https://jiancai.lgmi.com/html.aspx?cityid=&articleid=A1102&recordno=231283&u=/huizong/2021/05/24/A1102_a0f951.htm";
        WebRequest webRequest1 = new WebRequest(new URL(articleUrl), HttpMethod.GET);
        String content = webClient.getPage(webRequest1).getWebResponse().getContentAsString();
        log.info("请求内容：{}，\n 是否含有文章内容：{}", content, content.contains("6270"));
    }

    /**
     * 卓创资讯——通过webclient模拟登录
     *
     * @throws Exception
     */
    private static void zcPreLogin() throws Exception {
        // webRequest请求参数
        String loginUrl = "https://steel.sci99.com/include/login.aspx";
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
        List<NameValuePair> param = new ArrayList<>();
        param.add(new NameValuePair("__EVENTTARGET", "ctl05"));
        param.add(new NameValuePair("__VIEWSTATE", "/wEPDwUKLTI1MjY0MDM3Mw9kFgICAw9kFgRmDw9kFgIeCW9ua2V5ZG93bgU+aWYoZXZlbnQua2V5Q29kZT09MTMpIHtkb2N1bWVudC5hbGwuTG9naW4xX0J0bl9Mb2dpbi5mb2N1cygpO31kAgEPD2QWAh8ABT5pZihldmVudC5rZXlDb2RlPT0xMykge2RvY3VtZW50LmFsbC5Mb2dpbjFfQnRuX0xvZ2luLmZvY3VzKCk7fWRkeNnjQspQREPdVVpsj+XgtPiaBWg="));
        param.add(new NameValuePair("__EVENTVALIDATION", "/wEdAAbf7l8OdmxqSEoCPpeksJBTdicwIMstYnN20wZWH/gF/foWvHPm8vPP7prsqCzifKjwWriCj8gV6CIJZW8YDaPWUsZheAEslh4OlJsRxjnnZUnpuNJj0GyYTp7sUai+sw+QzrdxU0NZa94jORm5I98J2F/KfQ=="));
        param.add(new NameValuePair("chemname", "bad_boy"));
        param.add(new NameValuePair("chempwd", "xulejun520."));
        param.add(new NameValuePair("cookieenable", "true"));
        webRequest.setRequestParameters(param);

        // 添加请求头访问登录接口
        WebClient webClient = getWebClient();
        webClient.addRequestHeader("origin", "https://www.sci99.com");
        webClient.addRequestHeader("authority", "www.sci99.com");
        webClient.getPage(webRequest).getWebResponse();
        StringBuilder cookies = new StringBuilder();
        webClient.getCookieManager().getCookies().forEach(cookies::append);
        log.info("cookie:{}", cookies);

        // 请求文章接口
        String articleUrl = "https://steel.sci99.com/news/38363817.html";
        WebRequest webRequest1 = new WebRequest(new URL(articleUrl), HttpMethod.GET);
        String content = webClient.getPage(webRequest1).getWebResponse().getContentAsString();
        log.info("请求内容：{}，\n 是否含有文章内容：{}", content, content.contains("热轧夜盘再次下挫"));
    }

    /**
     * HuTool工具发请求，登录
     */
    private static void httpRequest() {
        // 1. 账号密码以及其他参数，请求登录接口
        String loginUrl = "https://www.sci99.com/include/scilogin.aspx?RequestId=9c5f3df968a22d5b";
        Map<String, Object> loginParam = new HashMap<>();
        loginParam.put("__VIEWSTATE", "/wEPDwUJNzY1NDIxNzU3D2QWAgIBD2QWBAIBD2QWBAIBDxYCHglvbmtleWRvd24FNmlmKGV2ZW50LmtleUNvZGU9PTEzKSB7ZG9jdW1lbnQuYWxsLklCX0xvZ2luLmZvY3VzKCk7fWQCAw8WAh8ABTZpZihldmVudC5rZXlDb2RlPT0xMykge2RvY3VtZW50LmFsbC5JQl9Mb2dpbi5mb2N1cygpO31kAgMPFgIeB1Zpc2libGVoZGSPX5KwMO6/eDdU3492U57e5SVLcQ==");
        loginParam.put("__VIEWSTATEGENERATOR", "3E64B80B");
        loginParam.put("__EVENTVALIDATION", "/wEdAAWCokWDr75oaN25lWExCFXEdicwIMstYnN20wZWH/gF/foWvHPm8vPP7prsqCzifKgCSMFMOl/1tuOo8rehKg7bLOi4jkxTA6biUy950W8hAIe+X8BVKw9l4Twwdh0wJbbsDG19");
        loginParam.put("chemname", "bad_boy");
        loginParam.put("chempwd", "xulejun520.");
        loginParam.put("fl1", "0");
        loginParam.put("iflogin", "0");
        HttpResponse httpResponse1 = HttpRequest.post(loginUrl)
                .header("Cookie", "guid=380c8532-780d-96db-3977-d4582405df8e; UM_distinctid=17982bc0bfc3a1-0035b83075a1ca8-1a387940-1fa400-17982bc0bfd5fe; CNZZDATA1269807659=1170073202-1621486588-%7C1621563635; accessId=b101a8c0-85cc-11ea-b67c-831fe7f7f53e; pageViewNum=3; Hm_lvt_44c27e8e603ca3b625b6b1e9c35d712d=1621490569,1621566443; route=5381fa73df88cce076c9e01d13c9b378; ASP.NET_SessionId=s1pajqrbt5k24viicmmynmyu; STATReferrerIndexId=1; Hm_lpvt_44c27e8e603ca3b625b6b1e9c35d712d=1621566569; qimo_seosource_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; qimo_seokeywords_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=%E6%9C%AA%E7%9F%A5; href=https%3A%2F%2Fwww.sci99.com%2F; isCloseOrderZHLayer=0; STATcUrl=")
                .form(loginParam).setMaxRedirectCount(10).execute();
//        HttpResponse httpResponse1 = HttpRequest.post(loginUrl).form(loginParam).execute();
        String body = httpResponse1.body();
        // cookies
        List<HttpCookie> cookiesList = httpResponse1.getCookies();
        StringBuilder cookies = new StringBuilder();
        for (HttpCookie cookie : cookiesList) {
            cookies.append(cookie.toString() + ";");
        }
        log.info("一阶段：\n cookie：{}，\n body：{}", cookiesList, body);

        // 2. 重定向地址跳转
//        String redirectUrl1 = "http:" + body.substring(body.indexOf("href=\"") + 6, body.indexOf("\">here") - 7).replace("amp;", "");
//       HttpResponse httpResponse2 = HttpRequest.get(redirectUrl1).header("Cookie", cookies.toString()).execute();
//        String body2 = httpResponse2.body();
//        // cookies
//        List<HttpCookie> cookiesList1 = httpResponse1.getCookies();
//        StringBuilder cookies1 = new StringBuilder();
//        for (HttpCookie cookie : cookiesList1) {
//            cookies1.append(cookie.toString() + ";");
//        }
//        log.info("二阶段：\n cookie：{}，\n body：{}", httpResponse2.getCookies(), body2);

        // 3. 重定向地址跳转
//        String redirectUrl2 = "https://steel.sci99.com/include/login.aspx?Token=5bb3f576a8a3cc82&RequestId=e2bc1138fb453527";
//        HttpResponse httpResponse3 = HttpRequest.get(redirectUrl2)
//                .header("Connection", "keep-alive")
//                .header("Pragma", "no-cache")
//                .header("Cache-Control", "no-cache")
//                .header("Upgrade-Insecure-Requests", "1")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Sec-Fetch-Site", "same-origin")
//                .header("Sec-Fetch-Mode", "navigate")
//                .header("Sec-Fetch-User", "?1")
//                .header("Sec-Fetch-Dest", "iframe")
//                .header("sec-ch-ua-mobile", "?0")
//                .header("Referer", "https://steel.sci99.com/")
//                .header("Accept-Language","https://steel.sci99.com/include/login.aspx?RequestId=1ca1a5d8d4865c9c")
//                .header("Cookie","UM_distinctid=1798906cf08904-0e3bf2915180ea-343c5606-1fa400-1798906cf099ef; guid=e13257f2-9a55-330e-54fd-241b4235a3d6; Hm_lvt_44c27e8e603ca3b625b6b1e9c35d712d=1621501596; route=5381fa73df88cce076c9e01d13c9b378; ASP.NET_SessionId=qilhs0giz53se2ovwsdytgvm; CNZZDATA1262024308=653814447-1621499858-https%253A%252F%252Fwww.sci99.com%252F%7C1621553745; STATReferrerIndexId=1; isCloseOrderZHLayer=0; qimo_seosource_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=%E7%AB%99%E5%86%85; qimo_seokeywords_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=; accessId=b101a8c0-85cc-11ea-b67c-831fe7f7f53e; href=https%3A%2F%2Fsteel.sci99.com%2F; pageViewNum=2; Hm_lpvt_44c27e8e603ca3b625b6b1e9c35d712d=1621557372; Hm_lvt_a831ffced6eb4eda5078ef1076222e03=1621501602,1621501641,1621557380,1621557389; Hm_lpvt_a831ffced6eb4eda5078ef1076222e03=1621557389")
//                .execute();
//        String body3 = httpResponse3.body();
//        log.info("三阶段：\n cookie：{}，\n body：{}", httpResponse3.getCookies(), body3);

        // 请求文章地址
//        HttpResponse articleResponse = HttpRequest.get("https://steel.sci99.com/news/38363817.html")
//                .header("Cookie", "UM_distinctid=1798906cf08904-0e3bf2915180ea-343c5606-1fa400-1798906cf099ef; guid=e13257f2-9a55-330e-54fd-241b4235a3d6; Hm_lvt_44c27e8e603ca3b625b6b1e9c35d712d=1621501596; route=5381fa73df88cce076c9e01d13c9b378; ASP.NET_SessionId=qilhs0giz53se2ovwsdytgvm; CNZZDATA1262024308=653814447-1621499858-https%253A%252F%252Fwww.sci99.com%252F%7C1621553745; STATReferrerIndexId=1; isCloseOrderZHLayer=0; qimo_seosource_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=%E7%AB%99%E5%86%85; qimo_seokeywords_b101a8c0-85cc-11ea-b67c-831fe7f7f53e=; href=https%3A%2F%2Fsteel.sci99.com%2F; accessId=b101a8c0-85cc-11ea-b67c-831fe7f7f53e; pageViewNum=2; Hm_lpvt_44c27e8e603ca3b625b6b1e9c35d712d=1621557372; Hm_lvt_a831ffced6eb4eda5078ef1076222e03=1621501602,1621501641,1621557380,1621557389; Hm_lpvt_a831ffced6eb4eda5078ef1076222e03=1621557389")
//                .execute();
//        log.info("文章内容是否显示：{}", articleResponse.body().contains("热轧夜盘再次下挫"));

//        imgCodeLogin();
    }

    /**
     * 模拟登录-图片验证码
     *
     * @throws Exception
     */
    private static void imgCodeLogin() throws Exception {
        // 登录url
        String loginPageUrl = "https://how2j.cn/frontloginPage?act=login";
        HttpResponse execute = HttpRequest.get(loginPageUrl).execute();
        // 初始cookei
        String initCookie = execute.getCookieStr();
        // 图片链接
        String imgurl = "https://how2j.cn/frontshowRandomImage";

        // 带着cookie去请求随机验证码图片地址
        InputStream imgStream = HttpRequest.get(imgurl).header("cookie", initCookie).execute().bodyStream();

        // 将图片转换成byte数组
        byte[] data = readInputStream(imgStream);
        // 保存图片到本地
        String pngPath = "C:\\Users\\DELL\\Desktop\\code.png";
        File pngFile = new File(pngPath);
        FileOutputStream outStream = new FileOutputStream(pngFile);
        outStream.write(data);
        outStream.close();
        // 通过本地手动输入验证码
        Scanner scanner = new Scanner(System.in);
        String inCode = scanner.next();
        // 带着账号、密码、验证码请求登录接口
        HttpResponse cookieResponse = HttpRequest.post("https://how2j.cn/frontlogin")
                .header("authority", "how2j.cn")
                .header("x-requested-with", "XMLHttpRequest")
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("origin", "https://how2j.cn")
                .header("sec-fetch-site", "same-origin")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-dest", "empty")
                .header("referer", "https://how2j.cn/frontloginPage?act=login")
                .header("cookie", initCookie)
                .body("user.email=xule_jun%40163.com&user.password=xulejun520&randomCode=" + inCode).execute();

        // 获取登录后的cookies
        List<HttpCookie> cookies = cookieResponse.getCookies();
        StringBuilder stringBuilder = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            stringBuilder.append(cookie.toString() + ";");
        }
        System.out.println(stringBuilder);
    }

    /**
     * 输入流转化成byte数组
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * webClient配置
     *
     * @return
     */
    private static WebClient getWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setTimeout(15 * 1000);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        // webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        return webClient;
    }
}

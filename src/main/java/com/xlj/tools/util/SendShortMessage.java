package com.xlj.tools.util;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.Map;

/**
 * 阿里云短信服务SDK：
 * 官网购买地址：https://market.aliyun.com/products/?spm=5176.9502607.searchList.4.5bbe4d50l0PcWj&keywords=%E7%9F%AD%E4%BF%A1
 *
 * @author legend xu
 * @date 2022/1/14
 */
@Slf4j
public class SendShortMessage {
    public static void main(String[] args) {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "d75aa2f17c2d49119d37438f4f86aae2";
        Map<String, String> headers = Maps.newHashMap();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = Maps.newHashMap();
        querys.put("mobile", "15870906323");
        String code = RandomUtil.randomNumbers(6);
        querys.put("param", "**code**:" + code + ",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = Maps.newHashMap();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = AliyunHttpUtil.doPost(host, path, method, headers, querys, bodys);
            log.info(response.toString());
            //获取response的body
            log.info(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

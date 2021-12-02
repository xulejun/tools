package com.xlj.tools.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 转换工具
 *
 * @author legend xu
 * @date 2021/12/2
 */
public class ConvertUtil {

    /**
     * .properties 配置文件转 hashMap
     *
     * @throws IOException
     */
    public static void propertiesConvertMap() throws IOException {
        String config = "data.input.cancelPolicy=入住前可免费取消,入住当天12:00前可免费取消,入住当天18:00前可免费取消,入住当天20:00前可免费取消,入住当天23:59前可免费取消,15分钟内可免费取消,不可取消,有条件取消,限时取消\n" +
                "data.input.payMethod=在线付,到店付\n" +
                "data.input.bedType=缺失,大床,双床,双人床,特大床,单人床,多床,上下铺,圆床,其他床,双床/大床,单人床/大床\n" +
                "data.input.window=缺失,无窗,有窗,落地窗\n" +
                "data.input.promotion.label=10亿补贴,新客专享红包,限时特惠,酒店天天神券,高星首单红包,高端酒店红包,今夜特价,惊喜津贴红包,叠加红包,出游特惠8折,牛气冲天红包,天天特价,随享红包,一口价208元,一口价328元,超值一口价,超值返现,特惠立减,限时5折,限时抢62折,6折放价红包,7折放价红包,8折放价红包,本店新客6折,本店新客6.1折,本店新客6.2折,本店新客6.3折,本店新客6.4折,本店新客6.5折,本店新客6.6折,本店新客6.7折,本店新客6.8折,本店新客6.9折,本店新客7折,本店新客7.1折,本店新客7.2折,本店新客7.3折,本店新客7.4折,本店新客7.5折,本店新客7.6折,本店新客7.6折,本店新客7.7折,本店新客7.8折,本店新客7.9折,本店新客8折,本店新客8.1折,本店新客8.2折,本店新客8.3折,本店新客8.4折,本店新客8.5折,本店新客8.6折,本店新客8.7折,本店新客8.8折,本店新客8.9折,本店新客9折,酒店随机红包,白银会员9.8折,黄金会员9.5折,铂金会员9折,铂金会员8.5折,钻石会员8.5折\n" +
                "data,input.source.channel=美团APP,美团微信APP小程序,美团微信PC小程序,大众点评APP,大众点评微信APP小程序,大众点评微信PC小程序";
        Properties properties = new Properties();
        properties.load(new StringReader(config));
        Map<Object, Object> hashMap = new HashMap<>(properties);
//        HashMap<String, String> hashMap = new HashMap<>((Map) properties);
        hashMap.keySet().forEach(System.out::println);
    }
}

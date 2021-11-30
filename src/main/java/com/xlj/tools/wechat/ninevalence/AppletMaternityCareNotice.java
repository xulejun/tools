package com.xlj.tools.wechat.ninevalence;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.xlj.tools.bean.ninevalence.MaternityCareAppletData;
import com.xlj.tools.bean.ninevalence.MaternityCareAppletInfo;
import com.xlj.tools.bean.ninevalence.MaternityCareAppletInfoDataList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 妇保小程序九价信息通知
 *
 * @author legend xu
 * @date 2021/11/29
 */
@Slf4j
@Component
public class AppletMaternityCareNotice {
    @Autowired
    private AccountNineValenceNotice accountNineValenceNotice;

    @Value("${mail.addressee}")
    private String addresseeStr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 小程序请求地址
     */
    public static final String MATERNITY_CARE_APPLET_URL = "https://netphs.eheren.com/phs-reg/reg/getDeptSch";
    /**
     * 请求体
     */
    public static final String MATERNITY_CARE_REQUEST_BODY = "{\"args\":{\"deptId\":\"101160103\",\"deptName\":\"HPV疫苗门诊(八一大道)\",\"hosId\":\"496\",\"specialtyId\":\"0502\",\"sysCode\":\"1010001\"},\"token\":\"\"}";

    public static final String CHECK_DOC_NAME = "进口九价首针号";
    private static final String SET_MATERNITY_CARE = "applet.maternityCare";

    /**
     * 获取数据并邮件通知
     */
    public void notice() {
        // 发送请求
        HttpResponse response = HttpRequest.post(MATERNITY_CARE_APPLET_URL).body(MATERNITY_CARE_REQUEST_BODY).timeout(15000).execute();
        // 数据组装
        Object dataList = new JSONObject(response.body()).getByPath("result.dataList");
        List<MaternityCareAppletInfoDataList> appletInfoDataLists = new JSONArray(dataList).toList(MaternityCareAppletInfoDataList.class);
        // 只获取最新的数据
        MaternityCareAppletInfoDataList infoDataList = appletInfoDataLists.get(appletInfoDataLists.size() - 1);
        for (MaternityCareAppletData infoData : infoDataList.getSchDateList()) {
            for (MaternityCareAppletInfo info : infoData.getSchemeList()) {
                // 场次名称
                String docName = info.getDocName();
                // 预约日期
                String schDate = info.getSchDate();
                // 预约状态
                String schStateName = info.getSchStateName();
                if (CHECK_DOC_NAME.equals(docName) && (!redisTemplate.opsForSet().isMember(SET_MATERNITY_CARE, schDate + "-" + schStateName))) {
                    String title = "江西省妇幼的保健院九价通知";
                    log.info("标题={}，场次名称={}，预约日期={}，预约状态={}，放号总量={}，剩余数量={}，", title, docName, schDate, schStateName, info.getNumHadReg(), info.getNumRemain());
                    String content = "<table width=\"80%\" border=\"1\" cellspacing=\"1\" cellpadding=\"4\" align=\"center\" style=\"margin-top: 50%;text-align: center\">\n" +
                            "        <tr>\n" +
                            "            <td style=\"color: #255e95\">场次名称</td>\n" +
                            "            <td>" + docName + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td style=\"color: #255e95\">预约日期</td>\n" +
                            "            <td>" + schDate + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td style=\"color: #255e95\">预约状态</td>\n" +
                            "            <td style=\"color: red\">" + schStateName + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td style=\"color: #255e95\">放号总量</td>\n" +
                            "            <td>" + info.getNumHadReg() + "</td>\n" +
                            "        </tr>\n" +
                            "        <tr>\n" +
                            "            <td style=\"color: #255e95\">剩余数量</td>\n" +
                            "            <td>" + info.getNumRemain() + "</td>\n" +
                            "        </tr>\n" +
                            "    </table>";
                    String[] addressee = addresseeStr.split(",");
                    // 发送邮件
                    accountNineValenceNotice.sendHtmlMail(title, addressee, content);
                    // 数据入库
                    redisTemplate.opsForSet().add(SET_MATERNITY_CARE, schDate + "-" + schStateName);
                }
            }
        }
    }
}

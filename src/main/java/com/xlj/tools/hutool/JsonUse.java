package com.xlj.tools.hutool;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.Date;

/**
 * hutool json工具使用
 * @author xlj
 * @date 2021/5/8 11:13
 */
public class JsonUse {
    public static void main(String[] args) {
        String jsons = "{'com':'zhongtong','data':[{'ftime':'2019-05-15 13:43:51','context':'【上海市】 快件已在 【九亭三部】 签收'},{'ftime':'2019-05-14 13:43:51','context':'【上海市】 快件已在 【九亭三部】 签收'}]}";
        // json对象转换
        JSONObject json = new JSONObject(jsons);
        // json表达式使用
        Object byPath = json.getByPath("data[0].ftime");
        System.out.println(byPath.toString());
        // json 数组转换
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            JSONObject data = array.getJSONObject(i);
            // json获取对应类型
            Date ftime = data.getDate("ftime");
            System.out.println(ftime.getTime());
        }
    }
}

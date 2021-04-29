package com.xlj.tools.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 时间计算
 *
 * @author xlj
 * @date 2021/4/29
 */
public class TimeCaculation {
    public static void main(String[] args) throws ParseException {
        weTogether();

        Scanner scanner = new Scanner(System.in);
        String start = scanner.next();

        // 开始时间字符串
        try {
            calculateTime(start);
        } catch (ParseException e) {
            System.out.println("请输入yyyy-MM-dd格式");
            scanner = new Scanner(System.in);
            start = scanner.next();
            calculateTime(start);
        }
    }

    public static void weTogether() throws ParseException {
        String start = "2018-10-12";
        calculateTime(start);
    }

    public static void calculateTime(String start) throws ParseException {
        // 当前时间字符串
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String end = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);

        // 开始时间 && 当前时间
        Date startTime = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date endTime = new SimpleDateFormat("yyyy-MM-dd").parse(end);

        long second = (endTime.getTime() - startTime.getTime()) / 1000;
//        System.out.println(second + " 秒");
        long minute = second / 60;
//        System.out.println(minute + " 分钟");
        long hour = minute / 60;
//        System.out.println(hour + " 小时");
        long allDay = hour / 24;
//        System.out.println(allDay + " 天");
        long year = allDay / 365;
        long overDay = allDay - year * 365;
//        System.out.println(year + " 年");
        long mouth = overDay / 30;
//        System.out.println(mouth + " 月");
        long day = overDay - mouth * 30;
//        System.out.println(day + " 天");
        System.out.println(year + "年 " + mouth + "月 " + day + "天 ");
        System.out.println("总天数：" + allDay);
        System.out.println("总小时：" + hour);
        System.out.println("总分钟：" + minute);
        System.out.println("总秒：" + second);
    }
}

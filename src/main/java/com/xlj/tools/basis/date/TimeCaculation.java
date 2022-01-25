package com.xlj.tools.basis.date;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * 时间计算
 *
 * @author xlj
 * @date 2021/4/29
 */
public class TimeCaculation {
    public static void main(String[] args) throws ParseException {
        weTogether();

        String exit = "";
        while (!"exit".equals(exit)) {
            System.out.println("请输入起始时间：");
            Scanner scanner = new Scanner(System.in);
            // 输入内容
            String start = scanner.next();
            exit = start;
            if (!"exit".equals(exit)) {
                try {
                    calculateTime(start);
                } catch (ParseException e) {
                    System.out.println("请输入yyyy-MM-dd格式");
                    scanner = new Scanner(System.in);
                    start = scanner.next();
                    calculateTime(start);
                }
            }
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
        long minute = second / 60;
        long hour = minute / 60;
        long allDay = hour / 24;
        long year = allDay / 365;
        long overDay = allDay - year * 365;
        long mouth = overDay / 30;
        long day = overDay - mouth * 30;
        System.out.println(start + " 距离现在已经过去了：");
        System.out.println(year + "年 " + mouth + "月 " + day + "天 ");
        System.out.println("总天数：" + allDay);
        System.out.println("总小时：" + hour);
        System.out.println("总分钟：" + minute);
        System.out.println("总秒：" + second);
    }

    /**
     * 计算昨天的日期
     */
    public static String yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    /**
     * 计算两个日期范围内的区间
     *
     * @param start           （yyyy-MM-dd）
     * @param end（yyyy-MM-dd）
     * @return （yyyy-MM-dd）具体日期 List
     */
    public static List<String> getDatesBetween(String start, String end) {
        String[] startDateArr = start.split("-");
        String[] endDateArr = end.split("-");
        // 设置开始日期
        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateArr[0]), Integer.parseInt(startDateArr[1]), Integer.parseInt(startDateArr[2]));
        // 结束日期
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateArr[0]), Integer.parseInt(endDateArr[1]), Integer.parseInt(endDateArr[2]));
        // 两个事件相隔天数
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        List<LocalDate> dateList = IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween)
                .mapToObj(startDate::plusDays).collect(Collectors.toList());
        return dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
    }

}

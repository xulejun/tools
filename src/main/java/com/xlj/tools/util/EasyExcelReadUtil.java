package com.xlj.tools.util;

import com.alibaba.excel.EasyExcel;
import com.xlj.tools.bean.ExportNews;
import com.xlj.tools.listener.TableListener;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

/**
 * easyExcel 读表格数据
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
public class EasyExcelReadUtil {
    public static void main(String[] args) throws FileNotFoundException {
        int sheetNum = 0;
        String fileName = "C:\\Users\\DELL\\Desktop\\微信公众号整理（20210323更新） - 副本-1 - 副本.xlsx";
        TableListener tableListener = new TableListener();
        try {
            EasyExcel.read(fileName, ExportNews.class, tableListener).sheet(sheetNum).doRead();
        } catch (Exception e) {
            log.error("异常信息：", e);
        }

        // 不通过监听器读文件
//        ExcelReaderBuilder read = EasyExcelFactory.read(new FileInputStream(new File(fileName)));
//        List<Map<Integer, String>> list = read.doReadAllSync();
    }
}

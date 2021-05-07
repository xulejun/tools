package com.xlj.tools.util;

import com.alibaba.excel.EasyExcel;
import com.xlj.tools.bean.ExportNews;
import com.xlj.tools.listener.TableListener;
import lombok.extern.slf4j.Slf4j;

/**
 * easyExcel 读表格数据
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
public class EasyExcelReadUtil {
    public static void main(String[] args) {
        int sheetNum = 0;
        String fileName = "C:\\Users\\DELL\\Desktop\\新闻源数据-2021-05-06.xlsx";
        TableListener tableListener = new TableListener();
        try {
            EasyExcel.read(fileName, ExportNews.class, tableListener).sheet(sheetNum).doRead();
        } catch (Exception e) {
            log.error("异常信息：", e);
        }
    }
}

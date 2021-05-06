package com.xlj.tools.util;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileOutputStream;

/**
 * poi表格解析生成sql脚本
 *
 * @author xlj
 * @date 2021/5/6
 */
public class ParseExcelUtil {
    public static void main(String[] args) throws Exception {
        sqlGenerate();
    }

    private static void sqlGenerate() throws Exception {
        // 输出的sql
        File deleteFile = new File("C:\\Users\\DELL\\Desktop\\alter.sql");
        FileOutputStream fileOutputStream = new FileOutputStream(deleteFile);

        // 解析的Excel文件
        File parseFile = new File("C:\\Users\\DELL\\Desktop\\1-6行情表单.xlsx");

        // Excel页数（从0开始）
        int sheetNo = 2;
        Workbook workbook = WorkbookFactory.create(parseFile);
        Sheet sheet = workbook.getSheetAt(sheetNo);
        int lastRowNum = sheet.getLastRowNum();

        // 行索引从0开始
        for (int i = 1; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            // 列索引从0开始
            String data = row.getCell(1).getStringCellValue();
            if (StrUtil.isBlank(data)) {
                break;
            }
            // sql脚本生成
            byte[] sqlByte = ("alter table " + data + " add(TABLE_ROW_ID nvarchar2(100));\n").getBytes();
            fileOutputStream.write(sqlByte);
        }
        System.out.println("sql脚本自动生成完毕");
    }
}

package com.xlj.tools.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.xlj.tools.bean.ExportNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 利用EasyExcel导出表格数据
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
@Controller
public class ExcelExportController {

    @GetMapping("/exportExcel")
    public String exportExcel() {
        return "exportExcel";
    }

    /**
     * 导出数据
     *
     * @param
     */
    @GetMapping(value = "/easyExcel")
    public void easyExcel(HttpServletResponse response) {
        try {
            // 获取数据
            List<ExportNews> data = createData();

            // 文件名
            String sourceName = "新闻源数据-" + LocalDate.now() + ".xls";
            // sheet名
            String sheetName = "新闻源数据";
            // 文件名防止乱码
            String fileName = new String(sourceName.getBytes(), StandardCharsets.ISO_8859_1);

            response.addHeader("Content-Disposition", "filename=" + fileName);
            ServletOutputStream out = response.getOutputStream();
            EasyExcelFactory.write(out, ExportNews.class).sheet(sheetName).doWrite(data);
        } catch (Exception e) {
            log.warn("导出表格异常:{}", e);
        }
    }

    /**
     * 创建表格测试数据
     *
     * @return
     */
    public static List<ExportNews> createData() {
        int size = 0;
        List<ExportNews> data = new ArrayList<>();
        while (size < 10) {
            ExportNews exportNews = new ExportNews();
            exportNews.setId(size);
            exportNews.setName("新闻源名称" + size);
            exportNews.setUrl("URL" + size);
            exportNews.setOperatorName("创建人" + size);

            if (size % 2 == 0) {
                exportNews.setCreateTime(0L);
                exportNews.setInfoType(1);
                exportNews.setStatus(0);
                exportNews.setCron("0 0/5 * * * ?");
            } else if (size % 3 == 0) {
                exportNews.setCreateTime(System.currentTimeMillis());
                exportNews.setInfoType(1);
                exportNews.setStatus(2);
                exportNews.setCron("0 * * * * ?");
            } else {
                exportNews.setCreateTime(System.currentTimeMillis());
                exportNews.setInfoType(1);
                exportNews.setStatus(3);
            }
            data.add(exportNews);
            size++;
        }
        return data;
    }
}

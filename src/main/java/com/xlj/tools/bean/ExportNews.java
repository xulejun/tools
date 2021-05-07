package com.xlj.tools.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.xlj.tools.convert.NewsCronConvert;
import com.xlj.tools.convert.NewsInfoTypeConvert;
import com.xlj.tools.convert.NewsStatusConvert;
import com.xlj.tools.convert.NewsTimeConvert;
import lombok.Data;


/**
 * 新闻源视图对象
 *
 * @author xlj
 * @date 2021/3/5
 */
@Data
@ColumnWidth(14)
public class ExportNews {
    /**
     * 新闻源ID
     */
    @ExcelProperty("新闻源ID")
    private Integer id;

    /**
     * 新闻源名称
     */
    @ExcelProperty("新闻源名称")
    private String name;

    /**
     * 新闻源URL
     */
    @ExcelProperty("新闻源URL")
    private String url;

    /**
     * 操作人
     */
    @ExcelProperty("操作人")
    private String operatorName;

    /**
     * 操作时间，converter对应自定义转换类，无需再对表格数据做处理，在导出时easyExcel自动处理
     */
    @ExcelProperty(value = "最新操作时间", converter = NewsTimeConvert.class)
    private Long createTime;

    @ExcelProperty(value = "状态", converter = NewsStatusConvert.class)
    private Integer status;

    @ExcelProperty(value = "资讯类型", converter = NewsInfoTypeConvert.class)
    private Integer infoType;

    @ExcelProperty(value = "采集频率", converter = NewsCronConvert.class)
    private String cron;
}
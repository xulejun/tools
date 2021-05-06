package com.xlj.tools.convert;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.xlj.tools.enums.CronExpressionEnum;

/**
 * @author xlj
 * @date 2021/3/12
 */
public class NewsCronConvert implements Converter {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Object convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // cron表达式处理
        String cellData = "";
        for (CronExpressionEnum expressionEnum : CronExpressionEnum.values()) {
            if (expressionEnum.getCronExpression().equals(value)) {
                cellData = expressionEnum.getFrequency();
                break;
            } else if (StrUtil.isNotBlank((String) value)) {
                cellData = "高级";
            }
        }
        return new CellData(cellData);
    }
}

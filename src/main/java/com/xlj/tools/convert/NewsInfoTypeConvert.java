package com.xlj.tools.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.xlj.tools.enums.InfoTypeEnum;

/**
 * @author xlj
 * @date 2021/3/12
 */
public class NewsInfoTypeConvert implements Converter {
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
        for (InfoTypeEnum infoTypeEnum : InfoTypeEnum.values()) {
            if (value.equals(infoTypeEnum.getVal())) {
                return new CellData(infoTypeEnum.getDesc());
            }
        }
        return null;
    }
}

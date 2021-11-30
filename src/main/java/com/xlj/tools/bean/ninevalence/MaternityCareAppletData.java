package com.xlj.tools.bean.ninevalence;

import java.util.List;

/**
 * 当前日期所含有的预约场次信息
 *
 * @author legend xu
 * @String 2021/11/29
 */
public class MaternityCareAppletData {
    private String schDate;
    private String categorName;
    private List<MaternityCareAppletInfo> schemeList;
    private String categorCode;

    public void setSchDate(String schDate) {
        this.schDate = schDate;
    }

    public String getSchDate() {
        return schDate;
    }

    public void setCategorName(String categorName) {
        this.categorName = categorName;
    }

    public String getCategorName() {
        return categorName;
    }

    public void setSchemeList(List<MaternityCareAppletInfo> schemeList) {
        this.schemeList = schemeList;
    }

    public List<MaternityCareAppletInfo> getSchemeList() {
        return schemeList;
    }

    public void setCategorCode(String categorCode) {
        this.categorCode = categorCode;
    }

    public String getCategorCode() {
        return categorCode;
    }
}

package com.xlj.tools.bean.ninevalence;

import java.util.List;

/**
 * 当前日期所包含的信息
 *
 * @author legend xu
 * @String 2021/11/29
 */
public class MaternityCareAppletInfoDataList {
    private String schDate;
    private List<MaternityCareAppletData> schDateList;

    public void setSchDate(String schDate) {
        this.schDate = schDate;
    }

    public String getSchDate() {
        return schDate;
    }

    public void setSchDateList(List<MaternityCareAppletData> schDateList) {
        this.schDateList = schDateList;
    }

    public List<MaternityCareAppletData> getSchDateList() {
        return schDateList;
    }
}

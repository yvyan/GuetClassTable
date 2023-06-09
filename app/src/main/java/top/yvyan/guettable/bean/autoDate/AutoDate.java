package top.yvyan.guettable.bean.autoDate;

import java.util.List;

public class AutoDate {

    private int code;
    private int updateTime;
    private List<DateInfo> dateList;

    public void setDateList(List<DateInfo> dateList) {
        this.dateList = dateList;
    }
    public List<DateInfo> getDateList() {
        return dateList;
    }
}

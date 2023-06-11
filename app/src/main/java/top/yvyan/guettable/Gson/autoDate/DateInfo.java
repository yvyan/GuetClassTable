package top.yvyan.guettable.Gson.autoDate;

import java.util.Date;

public class DateInfo {

    private Date showTime;
    private Date startTime;
    private Date endTime;
    private String term;
    private String addTerm;
    private int weekNum;

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
    }

    public String getAddTerm() {
        return addTerm;
    }

    public Date getShowTime() {
        return showTime;
    }
}

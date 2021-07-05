package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.PlannedCourseBean;

public class EffectiveCredit {
    private String cname;
    private String engname;
    private String tname;
    private String term;
    private double planxf;
    private String sterm;
    private String scname;
    private float score;
    private String zpxs;
    private double xf;
    private String stp;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public PlannedCourseBean toPlannedCourseBean() {
        return new PlannedCourseBean(scname, String.valueOf(planxf), String.valueOf(score), stp, tname, false);
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }

    public String getEngname() {
        return engname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTname() {
        return tname;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void setPlanxf(double planxf) {
        this.planxf = planxf;
    }

    public double getPlanxf() {
        return planxf;
    }

    public void setSterm(String sterm) {
        this.sterm = sterm;
    }

    public String getSterm() {
        return sterm;
    }

    public void setScname(String scname) {
        this.scname = scname;
    }

    public String getScname() {
        return scname;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public void setZpxs(String zpxs) {
        this.zpxs = zpxs;
    }

    public String getZpxs() {
        return zpxs;
    }

    public void setXf(double xf) {
        this.xf = xf;
    }

    public double getXf() {
        return xf;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public String getStp() {
        return stp;
    }

}

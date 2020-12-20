package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.PlannedCourseBean;

public class EffectiveCredit {
    private String name;
    private String cname;
    private String engname;
    private String engcj;
    private String tname;
    private String stid;
    private String term;
    private String courseid;
    private double planxf;
    private double credithour;
    private String coursetype;
    private int lvl;
    private String sterm;
    private String courseno;
    private String scid;
    private String scname;
    private float score;
    private String zpxs;
    private double xf;
    private String stp;

    public PlannedCourseBean toPlannedCourseBean() {
        return new PlannedCourseBean(scname, String.valueOf(planxf), String.valueOf(score), stp, tname);
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCname() {
        return cname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }
    public String getEngname() {
        return engname;
    }

    public void setEngcj(String engcj) {
        this.engcj = engcj;
    }
    public String getEngcj() {
        return engcj;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
    public String getTname() {
        return tname;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }
    public String getStid() {
        return stid;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }

    public void setPlanxf(double planxf) {
        this.planxf = planxf;
    }
    public double getPlanxf() {
        return planxf;
    }

    public void setCredithour(double credithour) {
        this.credithour = credithour;
    }
    public double getCredithour() {
        return credithour;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }
    public String getCoursetype() {
        return coursetype;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }
    public int getLvl() {
        return lvl;
    }

    public void setSterm(String sterm) {
        this.sterm = sterm;
    }
    public String getSterm() {
        return sterm;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }
    public String getScid() {
        return scid;
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

    @Override
    public String toString() {
        return "EffectiveCredits{" +
                "name='" + name + '\'' +
                ", cname='" + cname + '\'' +
                ", engname='" + engname + '\'' +
                ", engcj='" + engcj + '\'' +
                ", tname='" + tname + '\'' +
                ", stid='" + stid + '\'' +
                ", term='" + term + '\'' +
                ", courseid='" + courseid + '\'' +
                ", planxf=" + planxf +
                ", credithour=" + credithour +
                ", coursetype='" + coursetype + '\'' +
                ", lvl=" + lvl +
                ", sterm='" + sterm + '\'' +
                ", courseno='" + courseno + '\'' +
                ", scid='" + scid + '\'' +
                ", scname='" + scname + '\'' +
                ", score=" + score +
                ", zpxs='" + zpxs + '\'' +
                ", xf=" + xf +
                ", stp='" + stp + '\'' +
                '}';
    }
}

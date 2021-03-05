package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.PlannedCourseBean;

public class PlannedCourse {
    private String tname;
    private String cname;
    private String credithour;
    private String score;
    private String term;
    private String sterm;
    private String scname;
    private String courseid;
    private String xf;

    public PlannedCourseBean toPlannedCourseBean() {
        return new PlannedCourseBean(cname, credithour, score, "1", "必修课程");
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTname() {
        return tname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }

    public void setCredithour(String credithour) {
        this.credithour = credithour;
    }

    public String getCredithour() {
        return credithour;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
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

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getXf() {
        return xf;
    }
}

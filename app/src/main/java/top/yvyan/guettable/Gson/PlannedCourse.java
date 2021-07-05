package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.PlannedCourseBean;

public class PlannedCourse {
    private String tname;
    private String cname;
    private String credithour;
    private String score;
    private String term;
    private String scname;
    private String courseid;

    public PlannedCourseBean toPlannedCourseBean() {
        boolean canRip = (tname.equals("专业任选") || tname.equals("专业限选"));
        return new PlannedCourseBean(cname, credithour, score, "1", "必修课程", canRip);
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
}

package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.PlannedCourseBean;

public class PlannedCourse {
    private String dptno;
    private String dptname;
    private String spno;
    private String spname;
    private String stid;
    private String grade;
    private String classno;
    private String name;
    private String coursetype;
    private String tname;
    private String cname;
    private String credithour;
    private String score;
    private String zpxs;
    private String term;
    private String sterm;
    private String scname;
    private String courseid;
    private String courseno;
    private String stp;
    private String xf;
    private String chk;

    public PlannedCourseBean toPlannedCourseBean() {
        return new PlannedCourseBean(cname, credithour, score, "1", "必修课程");
    }

    public void setDptno(String dptno) {
        this.dptno = dptno;
    }
    public String getDptno() {
        return dptno;
    }

    public void setDptname(String dptname) {
        this.dptname = dptname;
    }
    public String getDptname() {
        return dptname;
    }

    public void setSpno(String spno) {
        this.spno = spno;
    }
    public String getSpno() {
        return spno;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }
    public String getSpname() {
        return spname;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }
    public String getStid() {
        return stid;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }
    public String getClassno() {
        return classno;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCoursetype(String coursetype) {
        this.coursetype = coursetype;
    }
    public String getCoursetype() {
        return coursetype;
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

    public void setZpxs(String zpxs) {
        this.zpxs = zpxs;
    }
    public String getZpxs() {
        return zpxs;
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

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }
    public String getStp() {
        return stp;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }
    public String getXf() {
        return xf;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }
    public String getChk() {
        return chk;
    }

    @Override
    public String toString() {
        return "PlannedCourses{" +
                "dptno='" + dptno + '\'' +
                ", dptname='" + dptname + '\'' +
                ", spno='" + spno + '\'' +
                ", spname='" + spname + '\'' +
                ", stid='" + stid + '\'' +
                ", grade='" + grade + '\'' +
                ", classno='" + classno + '\'' +
                ", name='" + name + '\'' +
                ", coursetype='" + coursetype + '\'' +
                ", tname='" + tname + '\'' +
                ", cname='" + cname + '\'' +
                ", credithour='" + credithour + '\'' +
                ", score='" + score + '\'' +
                ", zpxs='" + zpxs + '\'' +
                ", term='" + term + '\'' +
                ", sterm='" + sterm + '\'' +
                ", scname='" + scname + '\'' +
                ", courseid='" + courseid + '\'' +
                ", courseno='" + courseno + '\'' +
                ", stp='" + stp + '\'' +
                ", xf='" + xf + '\'' +
                ", chk='" + chk + '\'' +
                '}';
    }
}

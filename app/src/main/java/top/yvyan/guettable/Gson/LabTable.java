package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.CourseBean;

public class LabTable {
    private String term;
    private String labid;
    private String itemname;
    private String courseid;
    private String cname;
    private String spno;
    private String spname;
    private String grade;
    private String teacherno;
    private String name;
    private String srname;
    private String srdd;
    private String xh;
    private int bno;
    private int persons;
    private int zc;
    private int xq;
    private int jc;
    private int jc1;
    private String assistantno;
    private String teachers;
    private String comm;
    private String courseno;
    private int stusct;
    private String srid;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLabid() {
        return labid;
    }

    public void setLabid(String labid) {
        this.labid = labid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSpno() {
        return spno;
    }

    public void setSpno(String spno) {
        this.spno = spno;
    }

    public String getSpname() {
        return spname;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTeacherno() {
        return teacherno;
    }

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrname() {
        return srname;
    }

    public void setSrname(String srname) {
        this.srname = srname;
    }

    public String getSrdd() {
        return srdd;
    }

    public void setSrdd(String srdd) {
        this.srdd = srdd;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public int getBno() {
        return bno;
    }

    public void setBno(int bno) {
        this.bno = bno;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public int getZc() {
        return zc;
    }

    public void setZc(int zc) {
        this.zc = zc;
    }

    public int getXq() {
        return xq;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }

    public int getJc() {
        return jc;
    }

    public void setJc(int jc) {
        this.jc = jc;
    }

    public int getJc1() {
        return jc1;
    }

    public void setJc1(int jc1) {
        this.jc1 = jc1;
    }

    public String getAssistantno() {
        return assistantno;
    }

    public void setAssistantno(String assistantno) {
        this.assistantno = assistantno;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getCourseno() {
        return courseno;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }

    public int getStusct() {
        return stusct;
    }

    public void setStusct(int stusct) {
        this.stusct = stusct;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public CourseBean toCourseBean() {
        CourseBean courseBean = new CourseBean();
        courseBean.setLab(cname, itemname, bno, srdd, zc, xq, jc, name, comm);
        return courseBean;
    }

    @Override
    public String toString() {
        return "LabTable{" +
                "term='" + term + '\'' +
                ", labid='" + labid + '\'' +
                ", itemname='" + itemname + '\'' +
                ", courseid='" + courseid + '\'' +
                ", cname='" + cname + '\'' +
                ", spno='" + spno + '\'' +
                ", spname='" + spname + '\'' +
                ", grade='" + grade + '\'' +
                ", teacherno='" + teacherno + '\'' +
                ", name='" + name + '\'' +
                ", srname='" + srname + '\'' +
                ", srdd='" + srdd + '\'' +
                ", xh='" + xh + '\'' +
                ", bno=" + bno +
                ", persons=" + persons +
                ", zc=" + zc +
                ", xq=" + xq +
                ", jc=" + jc +
                ", jc1=" + jc1 +
                ", assistantno='" + assistantno + '\'' +
                ", teachers='" + teachers + '\'' +
                ", comm='" + comm + '\'' +
                ", courseno='" + courseno + '\'' +
                ", stusct=" + stusct +
                ", srid='" + srid + '\'' +
                '}';
    }
}

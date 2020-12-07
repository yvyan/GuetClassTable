package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.bean.ExperimentScoreBean;

public class ExperimentScore {
    private String name;
    private String cname;
    private String tname;
    private String spno;
    private String spname;
    private String grade;
    private String stid;
    private String term;
    private String labid;
    private int testtime;
    private String courseid;
    private String chk;
    private float pscj;
    private float khcj;
    private float zpcj;
    private String cjlb;
    private String kslb;
    private String astype;
    private String teacherno;
    private String comm;

    public ExperimentScoreBean toExperimentScoreBean() {
        return new ExperimentScoreBean(cname, labid, term, zpcj, pscj, khcj);
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

    public void setTname(String tname) {
        this.tname = tname;
    }
    public String getTname() {
        return tname;
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

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
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

    public void setLabid(String labid) {
        this.labid = labid;
    }
    public String getLabid() {
        return labid;
    }

    public void setTesttime(int testtime) {
        this.testtime = testtime;
    }
    public int getTesttime() {
        return testtime;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }
    public String getChk() {
        return chk;
    }

    public void setPscj(float pscj) {
        this.pscj = pscj;
    }
    public float getPscj() {
        return pscj;
    }

    public void setKhcj(float khcj) {
        this.khcj = khcj;
    }
    public float getKhcj() {
        return khcj;
    }

    public void setZpcj(float zpcj) {
        this.zpcj = zpcj;
    }
    public float getZpcj() {
        return zpcj;
    }

    public void setCjlb(String cjlb) {
        this.cjlb = cjlb;
    }
    public String getCjlb() {
        return cjlb;
    }

    public void setKslb(String kslb) {
        this.kslb = kslb;
    }
    public String getKslb() {
        return kslb;
    }

    public void setAstype(String astype) {
        this.astype = astype;
    }
    public String getAstype() {
        return astype;
    }

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
    }
    public String getTeacherno() {
        return teacherno;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
    public String getComm() {
        return comm;
    }
}

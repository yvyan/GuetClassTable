package top.yvyan.guettable.Gson;

import java.util.Date;

import top.yvyan.guettable.bean.ResitBean;

public class Resit {
    private String name;
    private String dptno;
    private String dptname;
    private String spno;
    private String spname;
    private String cname;
    private String croomname;
    private String grade;
    private String bj;
    private String kssj;
    private String comm;
    private int xq;
    private String examt;
    private String term;
    private int bkzt;
    private String courseno;
    private String stid;
    private String courseid;
    private Date examdate;
    private String examtime;
    private String croomno;
    private int seatno;
    private int zms;
    private int zrs;
    private int pc;
    private String lb;
    private int ks;

    public ResitBean toResitBean() {
        return new ResitBean(courseno, cname, kssj, examdate, croomno);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
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

    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCname() {
        return cname;
    }

    public void setCroomname(String croomname) {
        this.croomname = croomname;
    }
    public String getCroomname() {
        return croomname;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }
    public String getBj() {
        return bj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }
    public String getKssj() {
        return kssj;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
    public String getComm() {
        return comm;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }
    public int getXq() {
        return xq;
    }

    public void setExamt(String examt) {
        this.examt = examt;
    }
    public String getExamt() {
        return examt;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
    }

    public void setBkzt(int bkzt) {
        this.bkzt = bkzt;
    }
    public int getBkzt() {
        return bkzt;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }
    public String getStid() {
        return stid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }

    public void setExamdate(Date examdate) {
        this.examdate = examdate;
    }
    public Date getExamdate() {
        return examdate;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }
    public String getExamtime() {
        return examtime;
    }

    public void setCroomno(String croomno) {
        this.croomno = croomno;
    }
    public String getCroomno() {
        return croomno;
    }

    public void setSeatno(int seatno) {
        this.seatno = seatno;
    }
    public int getSeatno() {
        return seatno;
    }

    public void setZms(int zms) {
        this.zms = zms;
    }
    public int getZms() {
        return zms;
    }

    public void setZrs(int zrs) {
        this.zrs = zrs;
    }
    public int getZrs() {
        return zrs;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
    public int getPc() {
        return pc;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }
    public String getLb() {
        return lb;
    }

    public void setKs(int ks) {
        this.ks = ks;
    }
    public int getKs() {
        return ks;
    }
}

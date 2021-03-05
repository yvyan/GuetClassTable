package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ResitBean;

public class Resit {
    private String name;
    private String cname;
    private String croomname;
    private String bj;
    private String kssj;
    private String term;
    private int bkzt;
    private String courseno;
    private String courseid;
    private String examdate;
    private String examtime;
    private String croomno;
    private int seatno;
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

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getExamdate() {
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

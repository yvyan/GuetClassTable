package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ExamScoreBean;

public class ExamScore {
    private String dptno;
    private String dptname;
    private String spno;
    private String spname;
    private String bj;
    private int grade;
    private String stid;
    private String name;
    private String term;
    private String courseid;
    private String courseno;
    private String cname;
    private String courselevel;
    private int score;
    private String zpxs;
    private String kctype;
    private String typeno;
    private String cid;
    private String cno;
    private float sycj;
    private float qzcj;
    private float pscj;
    private float khcj;
    private int zpcj;
    private String kslb;
    private String cjlb;
    private int kssj;
    private float xf;
    private String xslb;
    private String tname1;
    private int stage;
    private String examt;
    private int xs;
    private int cjlx;
    private int chk;
    private String comm;

    public ExamScoreBean toExamScoreBean() {
        return new ExamScoreBean(cname, cno, term, zpxs, score, pscj, sycj, khcj, xf);
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

    public void setBj(String bj) {
        this.bj = bj;
    }
    public String getBj() {
        return bj;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
    public int getGrade() {
        return grade;
    }

    public void setStid(String stid) {
        this.stid = stid;
    }
    public String getStid() {
        return stid;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
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

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCname() {
        return cname;
    }

    public void setCourselevel(String courselevel) {
        this.courselevel = courselevel;
    }
    public String getCourselevel() {
        return courselevel;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }

    public void setZpxs(String zpxs) {
        this.zpxs = zpxs;
    }
    public String getZpxs() {
        return zpxs;
    }

    public void setKctype(String kctype) {
        this.kctype = kctype;
    }
    public String getKctype() {
        return kctype;
    }

    public void setTypeno(String typeno) {
        this.typeno = typeno;
    }
    public String getTypeno() {
        return typeno;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getCid() {
        return cid;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }
    public String getCno() {
        return cno;
    }

    public void setSycj(float sycj) {
        this.sycj = sycj;
    }
    public float getSycj() {
        return sycj;
    }

    public void setQzcj(float qzcj) {
        this.qzcj = qzcj;
    }
    public float getQzcj() {
        return qzcj;
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

    public void setZpcj(int zpcj) {
        this.zpcj = zpcj;
    }
    public int getZpcj() {
        return zpcj;
    }

    public void setKslb(String kslb) {
        this.kslb = kslb;
    }
    public String getKslb() {
        return kslb;
    }

    public void setCjlb(String cjlb) {
        this.cjlb = cjlb;
    }
    public String getCjlb() {
        return cjlb;
    }

    public void setKssj(int kssj) {
        this.kssj = kssj;
    }
    public int getKssj() {
        return kssj;
    }

    public void setXf(float xf) {
        this.xf = xf;
    }
    public float getXf() {
        return xf;
    }

    public void setXslb(String xslb) {
        this.xslb = xslb;
    }
    public String getXslb() {
        return xslb;
    }

    public void setTname1(String tname1) {
        this.tname1 = tname1;
    }
    public String getTname1() {
        return tname1;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
    public int getStage() {
        return stage;
    }

    public void setExamt(String examt) {
        this.examt = examt;
    }
    public String getExamt() {
        return examt;
    }

    public void setXs(int xs) {
        this.xs = xs;
    }
    public int getXs() {
        return xs;
    }

    public void setCjlx(int cjlx) {
        this.cjlx = cjlx;
    }
    public int getCjlx() {
        return cjlx;
    }

    public void setChk(int chk) {
        this.chk = chk;
    }
    public int getChk() {
        return chk;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
    public String getComm() {
        return comm;
    }
}

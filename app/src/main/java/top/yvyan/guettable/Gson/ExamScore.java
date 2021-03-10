package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ExamScoreBean;

public class ExamScore {
    private String name;
    private String term;
    private String cname;
    private float score;
    private String zpxs;
    private String typeno;
    private String cid;
    private String cno;
    private float sycj;
    private float qzcj;
    private float pscj;
    private float khcj;
    private float zpcj;
    private float xf;

    public ExamScoreBean toExamScoreBean() {
        return new ExamScoreBean(cid, cname, cno, term, zpxs, score, pscj, khcj, xf, typeno);
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

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCname() {
        return cname;
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

    public void setZpcj(float zpcj) {
        this.zpcj = zpcj;
    }

    public float getZpcj() {
        return zpcj;
    }

    public void setXf(float xf) {
        this.xf = xf;
    }

    public float getXf() {
        return xf;
    }
}

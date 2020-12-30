package top.yvyan.guettable.Gson;

// 教材评价表单信息
public class AvgTextbookFormGet {
    private String term;
    private String courseid;
    private int lsh;
    private int pjno;
    private String type;
    private String pzjb;
    private String zbnr;
    private double qz;
    private double score;
    private String dja;
    private String djb;
    private String djc;
    private String djd;
    private String a;
    private String b;
    private String c;
    private String d;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public int getLsh() {
        return lsh;
    }

    public void setLsh(int lsh) {
        this.lsh = lsh;
    }

    public int getPjno() {
        return pjno;
    }

    public void setPjno(int pjno) {
        this.pjno = pjno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPzjb() {
        return pzjb;
    }

    public void setPzjb(String pzjb) {
        this.pzjb = pzjb;
    }

    public String getZbnr() {
        return zbnr;
    }

    public void setZbnr(String zbnr) {
        this.zbnr = zbnr;
    }

    public double getQz() {
        return qz;
    }

    public void setQz(double qz) {
        this.qz = qz;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDja() {
        return dja;
    }

    public void setDja(String dja) {
        this.dja = dja;
    }

    public String getDjb() {
        return djb;
    }

    public void setDjb(String djb) {
        this.djb = djb;
    }

    public String getDjc() {
        return djc;
    }

    public void setDjc(String djc) {
        this.djc = djc;
    }

    public String getDjd() {
        return djd;
    }

    public void setDjd(String djd) {
        this.djd = djd;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "AvgTextbookFormGet{" +
                "term='" + term + '\'' +
                ", courseid='" + courseid + '\'' +
                ", lsh=" + lsh +
                ", pjno=" + pjno +
                ", type='" + type + '\'' +
                ", pzjb='" + pzjb + '\'' +
                ", zbnr='" + zbnr + '\'' +
                ", qz=" + qz +
                ", score=" + score +
                ", dja='" + dja + '\'' +
                ", djb='" + djb + '\'' +
                ", djc='" + djc + '\'' +
                ", djd='" + djd + '\'' +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                '}';
    }
}

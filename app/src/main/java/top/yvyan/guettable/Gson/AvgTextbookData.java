package top.yvyan.guettable.Gson;

// 教材评价数据
public class AvgTextbookData {
    private String term;
    private String userid;
    private String type;
    private int checked;
    private int lsh;
    private String courseid;
    private String dptno;
    private double score;
    private String comm;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getLsh() {
        return lsh;
    }

    public void setLsh(int lsh) {
        this.lsh = lsh;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getDptno() {
        return dptno;
    }

    public void setDptno(String dptno) {
        this.dptno = dptno;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
}

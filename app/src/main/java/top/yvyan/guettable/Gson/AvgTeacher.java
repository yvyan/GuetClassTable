package top.yvyan.guettable.Gson;

public class AvgTeacher {
    private String term;
    private String stid;
    private String courseid;
    private String teacherno;
    private String courseno;
    private String cname;
    private String name;
    private int lb;
    private int chk;
    private boolean can;
    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
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

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
    }
    public String getTeacherno() {
        return teacherno;
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

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setLb(int lb) {
        this.lb = lb;
    }
    public int getLb() {
        return lb;
    }

    public void setChk(int chk) {
        this.chk = chk;
    }
    public int getChk() {
        return chk;
    }

    public void setCan(boolean can) {
        this.can = can;
    }
    public boolean getCan() {
        return can;
    }

    @Override
    public String toString() {
        return "AvgTeacher{" +
                "term='" + term + '\'' +
                ", stid='" + stid + '\'' +
                ", courseid='" + courseid + '\'' +
                ", teacherno='" + teacherno + '\'' +
                ", courseno='" + courseno + '\'' +
                ", cname='" + cname + '\'' +
                ", name='" + name + '\'' +
                ", lb=" + lb +
                ", chk='" + chk + '\'' +
                ", can=" + can +
                '}';
    }
}

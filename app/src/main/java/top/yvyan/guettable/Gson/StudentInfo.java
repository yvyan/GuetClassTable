package top.yvyan.guettable.Gson;

/**
 * @clear
 */
public class StudentInfo {
    private String term;
    private String grade;
    private String dptno;
    private String dptname;
    private String spno;
    private String spname;
    private String stid;
    private String name;

    public String getTerm() {
        return term;
    }

    public String getGrade() {
        return grade;
    }

    public String getDptno() {
        return dptno;
    }

    public String getDptname() {
        return dptname;
    }

    public String getSpno() {
        return spno;
    }

    public String getSpname() {
        return spname;
    }

    public String getStid() {
        return stid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "term='" + term + '\'' +
                ", grade='" + grade + '\'' +
                ", dptno='" + dptno + '\'' +
                ", dptname='" + dptname + '\'' +
                ", spno='" + spno + '\'' +
                ", spname='" + spname + '\'' +
                ", stid='" + stid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

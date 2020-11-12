package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.CourseBean;

public class ClassTable {
    private long id;
    private String ctype;
    private String tname;
    private String examt;
    private String dptname;
    private String dptno;
    private String spname;
    private String spno;
    private String grade;
    private String cname;
    private String courseno;
    private String teacherno;
    private String name;
    private String term;
    private String courseid;
    private String croomno;
    private String comm;
    private int startweek;
    private int endweek;
    private boolean oddweek;
    private int week;
    private String seq;
    private long maxcnt;
    private double xf;
    private double llxs;
    private double syxs;
    private double sjxs;
    private double qtxs;
    private long sctcnt;
    private double hours;

    public long getId() {
        return id;
    }

    public String getCtype() {
        return ctype;
    }

    public String getTname() {
        return tname;
    }

    public String getExamt() {
        return examt;
    }

    public String getDptname() {
        return dptname;
    }

    public String getDptno() {
        return dptno;
    }

    public String getSpname() {
        return spname;
    }

    public String getSpno() {
        return spno;
    }

    public String getGrade() {
        return grade;
    }

    public String getCname() {
        return cname;
    }

    public String getCourseno() {
        return courseno;
    }

    public String getTeacherno() {
        return teacherno;
    }

    public String getName() {
        return name;
    }

    public String getTerm() {
        return term;
    }

    public String getCourseid() {
        return courseid;
    }

    public String getCroomno() {
        return croomno;
    }

    public String getComm() {
        return comm;
    }

    public int getStartweek() {
        return startweek;
    }

    public int getEndweek() {
        return endweek;
    }

    public boolean isOddweek() {
        return oddweek;
    }

    public int getWeek() {
        return week;
    }

    public String getSeq() {
        return seq;
    }

    public long getMaxcnt() {
        return maxcnt;
    }

    public double getXf() {
        return xf;
    }

    public double getLlxs() {
        return llxs;
    }

    public double getSyxs() {
        return syxs;
    }

    public double getSjxs() {
        return sjxs;
    }

    public double getQtxs() {
        return qtxs;
    }

    public long getSctcnt() {
        return sctcnt;
    }

    public double getHours() {
        return hours;
    }

    public CourseBean toCourseBean() {
        CourseBean courseBean = new CourseBean();
        courseBean.setCourse(courseno, cname, croomno, startweek, endweek, week, Integer.parseInt(seq), name);
        return courseBean;
    }

    @Override
    public String toString() {
        return "ClassTable{" +
                "id=" + id +
                ", ctype='" + ctype + '\'' +
                ", tname='" + tname + '\'' +
                ", examt='" + examt + '\'' +
                ", dptname='" + dptname + '\'' +
                ", dptno='" + dptno + '\'' +
                ", spname='" + spname + '\'' +
                ", spno='" + spno + '\'' +
                ", grade='" + grade + '\'' +
                ", cname='" + cname + '\'' +
                ", courseno='" + courseno + '\'' +
                ", teacherno='" + teacherno + '\'' +
                ", name='" + name + '\'' +
                ", term='" + term + '\'' +
                ", courseid='" + courseid + '\'' +
                ", croomno='" + croomno + '\'' +
                ", comm='" + comm + '\'' +
                ", startweek=" + startweek +
                ", endweek=" + endweek +
                ", oddweek=" + oddweek +
                ", week=" + week +
                ", seq='" + seq + '\'' +
                ", maxcnt=" + maxcnt +
                ", xf=" + xf +
                ", llxs=" + llxs +
                ", syxs=" + syxs +
                ", sjxs=" + sjxs +
                ", qtxs=" + qtxs +
                ", sctcnt=" + sctcnt +
                ", hours=" + hours +
                '}';
    }
}

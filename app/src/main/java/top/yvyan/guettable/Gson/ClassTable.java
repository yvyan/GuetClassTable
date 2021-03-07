package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.CourseBean;

public class ClassTable {
    private long id;
    private String ctype;
    private String cname;
    private String courseno;
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
    private long sctcnt;

    public long getId() {
        return id;
    }

    public String getCtype() {
        return ctype;
    }

    public String getCname() {
        return cname;
    }

    public String getCourseno() {
        return courseno;
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

    public long getSctcnt() {
        return sctcnt;
    }

    public CourseBean toCourseBean() {
        CourseBean courseBean = new CourseBean();
        courseBean.setCourse(courseno, cname, croomno, startweek, endweek, week, Integer.parseInt(seq), name, comm);
        return courseBean;
    }
}

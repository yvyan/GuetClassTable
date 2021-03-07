package top.yvyan.guettable.Gson;

import com.umeng.umcrash.UMCrash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import top.yvyan.guettable.bean.ExamBean;

public class ExamInfo {
    private String croomno;
    private String courseno;
    private String name;
    private String cname;
    private String examdate;
    private int zc;
    private int xq;
    private String ksjc;
    private String kssj;
    private String comm;

    public ExamBean toExamBean() {
        SimpleDateFormat format;
        if (examdate.contains("-")) {
            format = new SimpleDateFormat("yyyy-MM-dd");
        } else {
            format = new SimpleDateFormat("MM dd yyyy");
        }
        Date date = null;
        try {
            date = format.parse(examdate);
        } catch (ParseException e) {
            UMCrash.generateCustomLog(e, "ExamInfo.toExamBean");
        }
        return new ExamBean(courseno, cname, name, zc, xq, ksjc == null ? 0 : Integer.parseInt(ksjc), kssj, date, croomno, comm);
    }

    public void setCroomno(String croomno) {
        this.croomno = croomno;
    }

    public String getCroomno() {
        return croomno;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }

    public String getCourseno() {
        return courseno;
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

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setZc(int zc) {
        this.zc = zc;
    }

    public int getZc() {
        return zc;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }

    public int getXq() {
        return xq;
    }

    public void setKsjc(String ksjc) {
        this.ksjc = ksjc;
    }

    public String getKsjc() {
        return ksjc;
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
}

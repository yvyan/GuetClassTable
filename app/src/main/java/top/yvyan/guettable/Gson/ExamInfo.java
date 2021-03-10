package top.yvyan.guettable.Gson;

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
        return new ExamBean(courseno, cname, name, zc, xq, ksjc == null ? 0 : Integer.parseInt(ksjc), kssj, examdate, croomno, comm);
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
}

package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ResitBean;

public class Resit {
    private String cname;
    private String kssj;
    private String courseno;
    private String examdate;
    private String croomno;

    public ResitBean toResitBean() {
        return new ResitBean(courseno, cname, kssj, examdate, croomno);
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCname() {
        return cname;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }
}

package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.ExperimentScoreBean;

public class ExperimentScore {
    private String cname;
    private String term;
    private String labid;
    private String courseid;
    private float pscj;
    private float khcj;
    private float zpcj;

    public ExperimentScoreBean toExperimentScoreBean() {
        return new ExperimentScoreBean(cname, courseid, term, zpcj, pscj, khcj);
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLabid() {
        return labid;
    }

    public void setLabid(String labid) {
        this.labid = labid;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public float getPscj() {
        return pscj;
    }

    public void setPscj(float pscj) {
        this.pscj = pscj;
    }

    public float getKhcj() {
        return khcj;
    }

    public void setKhcj(float khcj) {
        this.khcj = khcj;
    }

    public float getZpcj() {
        return zpcj;
    }

    public void setZpcj(float zpcj) {
        this.zpcj = zpcj;
    }
}

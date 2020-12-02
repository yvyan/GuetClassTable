package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.CETBean;

public class CET {
    private String name;
    private String sex;
    private String postdate;
    private String dptno;
    private String dptname;
    private String spno;
    private String spname;
    private String grade;
    private String bj;
    private String term;
    private String stid;
    private String code;
    private float score;
    private int stage;
    private String card;
    private String operator;

    public CETBean toCETBean() {
        return new CETBean(code, term, stage, score, card, postdate);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getSex() {
        return sex;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
    public String getPostdate() {
        return postdate;
    }

    public void setDptno(String dptno) {
        this.dptno = dptno;
    }
    public String getDptno() {
        return dptno;
    }

    public void setDptname(String dptname) {
        this.dptname = dptname;
    }
    public String getDptname() {
        return dptname;
    }

    public void setSpno(String spno) {
        this.spno = spno;
    }
    public String getSpno() {
        return spno;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }
    public String getSpname() {
        return spname;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }
    public String getBj() {
        return bj;
    }

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

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setScore(float score) {
        this.score = score;
    }
    public float getScore() {
        return score;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
    public int getStage() {
        return stage;
    }

    public void setCard(String card) {
        this.card = card;
    }
    public String getCard() {
        return card;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getOperator() {
        return operator;
    }
}

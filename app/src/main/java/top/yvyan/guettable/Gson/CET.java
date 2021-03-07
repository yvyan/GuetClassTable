package top.yvyan.guettable.Gson;

import top.yvyan.guettable.bean.CETBean;

public class CET {
    private String postdate;
    private String grade;
    private String term;
    private String code;
    private float score;
    private int stage;
    private String card;

    public CETBean toCETBean() {
        return new CETBean(code, term, stage, score, card, postdate);
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
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

}

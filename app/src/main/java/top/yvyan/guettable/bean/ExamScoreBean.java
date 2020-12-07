package top.yvyan.guettable.bean;

import java.io.Serializable;

public class ExamScoreBean extends BeanAttribute implements Serializable {
    //课程名称
    private String name;
    //课号
    private String number;
    //学期
    private String term;
    //显示数据
    private String score;
    //总成绩
    private float totalScore;
    //平时成绩
    private float usuallyScore;
    //实验成绩
    private float experimentScore;
    //考核成绩
    private float checkScore;
    //学分
    private float credit;

    public ExamScoreBean() {}

    public ExamScoreBean(String name, String number, String term, String score, float totalScore, float usuallyScore, float experimentScore, float checkScore, float credit) {
        this.name = name;
        this.number = number;
        this.term = term;
        this.score = score;
        this.totalScore = totalScore;
        this.usuallyScore = usuallyScore;
        this.experimentScore = experimentScore;
        this.checkScore = checkScore;
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public float getUsuallyScore() {
        return usuallyScore;
    }

    public void setUsuallyScore(float usuallyScore) {
        this.usuallyScore = usuallyScore;
    }

    public float getExperimentScore() {
        return experimentScore;
    }

    public void setExperimentScore(float experimentScore) {
        this.experimentScore = experimentScore;
    }

    public float getCheckScore() {
        return checkScore;
    }

    public void setCheckScore(float checkScore) {
        this.checkScore = checkScore;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

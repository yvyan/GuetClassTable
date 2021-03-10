package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import com.umeng.umcrash.UMCrash;

import java.io.Serializable;

import top.yvyan.guettable.util.BeanAttributeUtil;

public class ExamScoreBean implements Serializable, BeanAttributeUtil.BeanAttribute {
    private static final long serialVersionUID = 1033222523316336312L;
    //课程序号
    private String cno;
    //课程名称
    private String name;
    //课号
    private String number;
    //学期
    private String term;
    //显示数据
    private String score;
    //总成绩
    private final float totalScore;
    //平时成绩
    private final float usuallyScore;
    //考核成绩
    private final float checkScore;
    //学分
    private final float credit;
    //类型
    private String type;

    public ExamScoreBean(String cno, String name, String number, String term, String score, float totalScore, float usuallyScore, float checkScore, float credit, String type) {
        this.cno = cno;
        this.name = name;
        this.number = number;
        this.term = term;
        this.score = score;
        this.totalScore = totalScore;
        this.usuallyScore = usuallyScore;
        //实验成绩
        this.checkScore = checkScore;
        this.credit = credit;
        this.type = type;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTerm() {
        if (term == null) {
            return "";
        }
        return term;
    }

    @Override
    public long getOrder() {
        try {
            if (getTerm().length() == 5) {
                return Integer.parseInt(getTerm()) * -1;
            }
            int year = Integer.parseInt(getTerm().substring(0, 4));
            year = year * 10 + Integer.parseInt(getTerm().substring(10, 11));
            return year * (-1);
        } catch (Exception e) {
            UMCrash.generateCustomLog(e, "ExamScoreBean.getOrder");
        }
        return 0;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public float getUsuallyScore() {
        return usuallyScore;
    }

    public float getCheckScore() {
        return checkScore;
    }

    public float getCredit() {
        return credit;
    }

    public String getScore() {
        if (score == null) {
            score = "";
        }
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNumber() {
        if (number == null) {
            number = "";
        }
        return number;
    }

    public String getCno() {
        if (cno == null) {
            cno = "";
        }
        return cno;
    }

    public String getType() {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        ExamScoreBean examScoreBean = (ExamScoreBean) obj;
        return this.getNumber().equals(examScoreBean.getNumber())
                && this.getScore().equals(examScoreBean.getScore())
                && this.usuallyScore == examScoreBean.usuallyScore
                && this.checkScore == examScoreBean.checkScore
                && this.credit == examScoreBean.credit;
    }
}

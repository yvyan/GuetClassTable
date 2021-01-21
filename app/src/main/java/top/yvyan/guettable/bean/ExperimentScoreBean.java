package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class ExperimentScoreBean implements Serializable, BeanAttribute {
    private static final long serialVersionUID = 7413694414885883651L;
    //实验名称
    private String name;
    //实验课程代码
    private String courseId;
    //学期
    private String term;
    //总评成绩
    private float finalScore;
    //平时成绩
    private float usuallyScore;
    //考核成绩
    private float checkScore;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        ExperimentScoreBean bean = (ExperimentScoreBean) obj;
        return courseId.equals(bean.courseId)
                && finalScore == bean.finalScore
                && checkScore == bean.checkScore;
    }

    public ExperimentScoreBean(String name, String courseId, String term, float finalScore, float usuallyScore, float checkScore) {
        this.name = name;
        this.courseId = courseId;
        this.term = term;
        this.finalScore = finalScore;
        this.usuallyScore = usuallyScore;
        this.checkScore = checkScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public long getOrder() {
        if (term.length() == 5) {
            return Integer.parseInt(term) * -1;
        }
        int year = Integer.parseInt(term.substring(0, 4));
        year = year * 10 + Integer.parseInt(term.substring(10, 11));
        return year * (-1);
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(float finalScore) {
        this.finalScore = finalScore;
    }

    public float getUsuallyScore() {
        return usuallyScore;
    }

    public void setUsuallyScore(float usuallyScore) {
        this.usuallyScore = usuallyScore;
    }

    public float getCheckScore() {
        return checkScore;
    }

    public void setCheckScore(float checkScore) {
        this.checkScore = checkScore;
    }
}

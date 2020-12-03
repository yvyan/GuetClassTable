package top.yvyan.guettable.bean;

public class ExamScoreBean {
    //课程名称
    private String name;
    //学期
    private String term;
    //总成绩
    private float totalScore;
    //平时成绩
    private float usuallyScore;
    //实验成绩
    private float experimentScore;
    //考核成绩
    private float checkScore;

    public ExamScoreBean() {}

    public ExamScoreBean(String name, String term, float totalScore, float usuallyScore, float experimentScore, float checkScore) {
        this.name = name;
        this.term = term;
        this.totalScore = totalScore;
        this.usuallyScore = usuallyScore;
        this.experimentScore = experimentScore;
        this.checkScore = checkScore;
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

    @Override
    public String toString() {
        return "ExamScoreBean{" +
                "name='" + name + '\'' +
                ", term='" + term + '\'' +
                ", totalScore=" + totalScore +
                ", usuallyScore=" + usuallyScore +
                ", experimentScore='" + experimentScore + '\'' +
                ", checkScore='" + checkScore + '\'' +
                '}';
    }
}

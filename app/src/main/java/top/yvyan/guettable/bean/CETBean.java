package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class CETBean implements Serializable, BeanAttribute {
    private static final long serialVersionUID = -4533762023709527528L;
    //等级名称 CET4 CET6
    private String name;
    //学期
    private String term;
    //综合成绩
    private int stage;
    //折算成绩
    private float score;
    //证书编号
    private String card;
    //推送时间
    private String postDate;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        CETBean cetBean = (CETBean) obj;
        return this.card.equals(cetBean.card)
                && this.stage == cetBean.stage;
    }

    public CETBean() {}

    public CETBean(String name, String term, int stage, float score, String card, String postDate) {
        this.name = name;
        this.term = term;
        this.stage = stage;
        this.score = score;
        this.card = card;
        this.postDate = postDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public long getOrder() {
        int year = Integer.parseInt(term.substring(0, 4));
        year = year * 10 + Integer.parseInt(term.substring(10, 11));
        return year;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return "CETBean{" +
                "name='" + name + '\'' +
                ", term='" + term + '\'' +
                ", stage=" + stage +
                ", score=" + score +
                ", card='" + card + '\'' +
                ", postDate='" + postDate + '\'' +
                '}';
    }
}

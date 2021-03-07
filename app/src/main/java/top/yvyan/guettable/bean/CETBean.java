package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class CETBean implements Serializable, BeanAttribute {
    private static final long serialVersionUID = -4533762023709527528L;
    //等级名称 CET4 CET6
    private String name;
    //学期
    private String term;
    //综合成绩
    private final int stage;
    //折算成绩
    private float score;
    //证书编号
    private final String card;
    //推送时间
    private final String postDate;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        CETBean cetBean = (CETBean) obj;
        if (cetBean.getCard() == null && this.getCard() != null || cetBean.getCard() != null && this.getCard() == null) {
            return false;
        }
        return this.card.equals(cetBean.card)
                && this.stage == cetBean.stage;
    }

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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCard() {
        return card;
    }

    public String getPostDate() {
        return postDate;
    }

    @NotNull
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

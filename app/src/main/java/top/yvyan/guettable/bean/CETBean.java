package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import com.umeng.umcrash.UMCrash;

import java.io.Serializable;

import top.yvyan.guettable.util.CourseUtil;

public class CETBean implements Serializable, CourseUtil.BeanAttributeUtil.BeanAttribute {
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
        return getCard().equals(cetBean.getCard())
                && getPostDate().equals(cetBean.getPostDate())
                && getTerm().equals(cetBean.getTerm())
                && getStage() == cetBean.getStage();
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
            term = "";
        }
        return term;
    }

    @Override
    public long getOrder() {
        int year = Integer.parseInt(term.substring(0, 4));
        try {
            return year * 10 + Integer.parseInt(term.substring(10, 11));
        } catch (Exception e) {
            try {
                return year * 10 + Integer.parseInt(term.substring(4, 5));
            } catch (Exception e1) {
                UMCrash.generateCustomLog(e1, "CETBean.getOrder");
            }
        }
        return 0;
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
        if (card == null) {
            card = "";
        }
        return card;
    }

    public String getPostDate() {
        if (postDate == null) {
            postDate = "";
        }
        return postDate;
    }
}

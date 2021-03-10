package top.yvyan.guettable.bean;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class PlannedCourseBean implements Serializable {
    private static final long serialVersionUID = -1789634455581353049L;
    private String name;
    private String credits;
    private String degree;
    private String type;
    private String typeName;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;

        PlannedCourseBean bean = (PlannedCourseBean) obj;
        return getDegree().equals(bean.getDegree())
                && getName().equals(bean.getName())
                && getCredits().equals(bean.getCredits())
                && getType().equals(bean.getType());
    }

    public PlannedCourseBean(String name, String credits, String degree, String type, String typeName) {
        this.name = name;
        this.credits = credits;
        this.degree = degree;
        this.type = type;
        this.typeName = typeName;
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

    public String getCredits() {
        if (credits == null) {
            credits = "";
        }
        return credits;
    }

    public String getDegree() {
        if (degree == null) {
            degree = "";
        }
        return degree;
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

    public String getTypeName() {
        if (typeName == null) {
            typeName = "";
        }
        return typeName;
    }
}

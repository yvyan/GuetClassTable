package top.yvyan.guettable.bean;

import java.io.Serializable;

public class PlannedCourseBean implements Serializable {
    private String name;
    private String credits;
    private String degree;
    private String type;
    private String typeName;

    public PlannedCourseBean(String name, String credits, String degree, String type, String typeName) {
        this.name = name;
        this.credits = credits;
        this.degree = degree;
        this.type = type;
        this.typeName = typeName;
    }

    public PlannedCourseBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "PlannedCourseBean{" +
                "name='" + name + '\'' +
                ", credits='" + credits + '\'' +
                ", degree='" + degree + '\'' +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}

package top.yvyan.guettable.bean;

import java.io.Serializable;

import top.yvyan.guettable.Gson.SelectedCourse;

public class SelectedCourseBean implements Serializable, BeanAttribute {

    private static final long serialVersionUID = -1665366365756246306L;

    private double courseCredit;
    private String courseName;
    private String selectType;
    private String courseQuality;

    public SelectedCourseBean(SelectedCourse selectedCourse) {
        this.courseCredit = selectedCourse.getXf();
        this.courseName = selectedCourse.getCname();
        this.selectType = selectedCourse.getStype();
        this.courseQuality = selectedCourse.getTname();
    }

    public double getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(double courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getCourseQuality() {
        return courseQuality;
    }

    public void setCourseQuality(String courseQuality) {
        this.courseQuality = courseQuality;
    }

    @Override
    public String getTerm() {
        return null;
    }

    @Override
    public long getOrder() {
        return (int) (courseCredit * 100);
    }
}

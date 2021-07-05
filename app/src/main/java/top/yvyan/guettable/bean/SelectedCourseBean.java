package top.yvyan.guettable.bean;

import java.io.Serializable;

import top.yvyan.guettable.Gson.SelectedCourse;
import top.yvyan.guettable.util.CourseUtil;

public class SelectedCourseBean implements Serializable, CourseUtil.BeanAttributeUtil.BeanAttribute {

    private static final long serialVersionUID = -1665366365756246306L;

    private final double courseCredit;
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

    public String getCourseName() {
        if (courseName == null) {
            courseName = "";
        }
        return courseName;
    }

    public String getSelectType() {
        if (selectType == null) {
            selectType = "";
        }
        return selectType;
    }

    public String getCourseQuality() {
        if (courseQuality == null) {
            courseQuality = "";
        }
        return courseQuality;
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

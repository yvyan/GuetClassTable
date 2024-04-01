package top.yvyan.guettable.bean;

import java.io.Serializable;

import top.yvyan.guettable.Gson.SelectedCourse;
import top.yvyan.guettable.util.CourseUtil;

public class SelectedCourseBean implements Serializable, CourseUtil.BeanAttributeUtil.BeanAttribute {

    private static final long serialVersionUID = -1665366365756246306L;

    public final double courseCredit;
    private String courseName;
    private String selectType;
    private String courseQuality;

    public int semesterId;
    private String semester;

    public String code;

    public String courseCode;

    public String teacher;

    public String remark;

    public SelectedCourseBean(Double courseCredit, String courseName, String teacher, String selectType, String courseQuality, String CourseCode, String Code, int semesterId, String semester, String remark) {
        this.courseCredit = courseCredit;
        this.teacher = teacher;
        this.courseName = courseName;
        this.selectType = selectType;
        this.courseQuality = courseQuality;
        this.code = Code;
        this.courseCode = CourseCode;
        this.semesterId = semesterId;
        this.semester = semester;
        this.remark = remark;
    }

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

    public String getSemester() {
        if (semester == null) {
            semester = "";
        }
        return semester;
    }

    @Override
    public String getTerm() {
        return null;
    }

    @Override
    public long getOrder() {
        return (int) (this.semesterId * 100000 + courseCredit * 100);
    }
}

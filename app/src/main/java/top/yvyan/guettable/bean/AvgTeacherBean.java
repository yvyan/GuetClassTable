package top.yvyan.guettable.bean;

public class AvgTeacherBean {
    String courseName;
    String teacherName;
    String hint;

    public AvgTeacherBean(String courseName, String teacherName, String hint) {
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.hint = hint;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public String getTeacherName() {
        return teacherName;
    }
}

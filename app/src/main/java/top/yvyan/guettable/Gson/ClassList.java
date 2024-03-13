package top.yvyan.guettable.Gson;


import java.util.List;

import top.yvyan.guettable.bean.SelectedCourseBean;

public class ClassList {
    public List<ClassInfo> lessons;
    public static class ClassInfo {
        public String lessonKindText;
        public CourseInfo course;
        public CourseType courseType;
        public Semester semester;
        public String code; // 课号
        public String teacherAssignmentString;

        private static class CourseInfo {
            public String code; // 课程代码
            public double credits;
            public String nameZh;
        }

        private static class CourseType {
            public String name;
        }
        public SelectedCourseBean toSelectedCourseBean() {
            return new SelectedCourseBean(course.credits, course.nameZh,teacherAssignmentString,lessonKindText,courseType.name,course.code,code, semester.id, semester.nameZh);
        }
    }
}

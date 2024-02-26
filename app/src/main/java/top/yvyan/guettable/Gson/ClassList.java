package top.yvyan.guettable.Gson;


import java.util.List;

import top.yvyan.guettable.bean.SelectedCourseBean;

public class ClassList {
    public List<ClassInfo> lessons;
    public static class ClassInfo {
        public String lessonKindText;
        public CourseInfo course;
        public CourseType courseType;
        private static class CourseInfo {
            public double credits;
            public String nameZh;
        }

        private static class CourseType {
            public String name;
        }
        public SelectedCourseBean toSelectedCourseBean() {
            return new SelectedCourseBean(course.credits, course.nameZh,lessonKindText,courseType.name);
        }
    }
}

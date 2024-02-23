package top.yvyan.guettable.Gson;

import static java.lang.Integer.parseInt;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.SelectedCourseBean;

public class ClassTableNew {
    public List<studentTableVms> studentTableVms;
    public static class studentTableVms {
        public List<ClassTable> activities;


    }
    public static class LessonSearchVms {
        public class Course {
            private String nameZh;
            private Double credits;
        }
        public static class CourseType {
            public String nameZh;
        }
        public String lessonKindText;
    }
    public static class ClassTable {
        private CourseType courseType;
        public static class CourseType {
            public String name;
        }
        private String lessonCode;
        private String courseName;
        private String room;
        private List<String> teachers;
        private String weeksStr;
        private Integer startUnit;
        private Double credits;
        private Integer weekday;
        private String lessonKindText;
        private String lessonRemark;
        public CourseBean toCourseBean() {
            CourseBean courseBean = new CourseBean();
            String[] weekRange=weeksStr.split("~");
            courseBean.setCourse(lessonCode, courseName, room,parseInt(weekRange[0]) , parseInt(weekRange.length==1 ? weekRange[0]:weekRange[1]), weekday, ((startUnit+1)/2), String.join(" ",teachers), lessonRemark);
            return courseBean;
        }

        public SelectedCourseBean toSelectedCourseBean() {
            SelectedCourseBean courseBean = new SelectedCourseBean(credits,courseName,lessonKindText,courseType.name);
            return courseBean;
        }

    }

}

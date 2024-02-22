package top.yvyan.guettable.Gson;

import static java.lang.Integer.parseInt;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;

public class ClassTableNew {
    public studentTableVms studentTableVms;
    private static class studentTableVms {
        public ClassTable[] activities;
    }
    private static class ClassTable {
        // 课号
        private String lessonCode;
        private String courseName;
        private String room;
        private String[] teachers;
        private String weeksStr;
        private Integer startUnit;
        private Integer weekday;

        public CourseBean toCourseBean() {
            CourseBean courseBean = new CourseBean();
            String[] weekRange=weeksStr.split("~");
            courseBean.setCourse(lessonCode, courseName, room,parseInt(weekRange[0]) , parseInt(weekRange[1]), weekday, startUnit/2, courseName, "");
            return courseBean;
        }
    }

}

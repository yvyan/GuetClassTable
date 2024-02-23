package top.yvyan.guettable.Gson;

import static java.lang.Integer.parseInt;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;

public class ClassTableNew {
    public List<studentTableVms> studentTableVms;
    public static class studentTableVms {
        public List<ClassTable> activities;
    }
    public static class ClassTable {
        // 课号
        private String lessonCode;
        private String courseName;
        private String room;
        private List<String> teachers;
        private String weeksStr;
        private Integer startUnit;
        private Integer weekday;

        public CourseBean toCourseBean() {
            CourseBean courseBean = new CourseBean();
            String[] weekRange=weeksStr.split("~");
            courseBean.setCourse(lessonCode, courseName, room,parseInt(weekRange[0]) , parseInt(weekRange.length==1 ? weekRange[0]:weekRange[1]), weekday, ((startUnit+1)/2), String.join(" ",teachers), "");
            return courseBean;
        }
    }

}

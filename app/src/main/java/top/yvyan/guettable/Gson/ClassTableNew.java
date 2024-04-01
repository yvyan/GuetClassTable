package top.yvyan.guettable.Gson;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.Arrays;
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
        private String startTime;
        private String endTime;
        private int[] weekIndexes;
        private Integer startUnit;
        private Integer endUnit;
        private Double credits;
        private Integer weekday;
        private String lessonRemark;

        public String getCourseTime() {
            if (startTime != null && !startTime.isEmpty() &&
                    endTime != null && !endTime.isEmpty()) {
                return startTime + "~" + endTime;
            }
            return null;
        }

        public List<CourseBean> toCourseBean() {
            if (lessonRemark == null) {
                lessonRemark = "";
            }
            List<CourseBean> courseBeans = new ArrayList<>();
            Arrays.sort(weekIndexes);
            int lastWeekIndex = weekIndexes[0];
            int startWeek = lastWeekIndex;
            for (int week : weekIndexes) {
                if (week - lastWeekIndex > 1) {
                    CourseBean courseBean = new CourseBean();
                    courseBean.setCourse(lessonCode, courseName, room, startWeek, lastWeekIndex, weekday, startUnit, endUnit, this.getCourseTime(), String.join(" ", teachers), lessonRemark);
                    courseBeans.add(courseBean);
                    startWeek = week;
                }
                lastWeekIndex = week;
            }
            CourseBean courseBean = new CourseBean();
            courseBean.setCourse(lessonCode, courseName, room, startWeek, lastWeekIndex, weekday, startUnit, endUnit, this.getCourseTime(), String.join(" ", teachers), lessonRemark);
            courseBeans.add(courseBean);
            return courseBeans;
        }

    }

}

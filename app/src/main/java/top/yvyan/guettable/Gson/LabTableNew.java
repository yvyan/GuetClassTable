package top.yvyan.guettable.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import top.yvyan.guettable.bean.CourseBean;

public class LabTableNew {
    public boolean success;
    public List<Result> result;

    public List<CourseBean> toCourseBeans(Map<String, String> getLessonCode) {
        if (!success) {
            return null;
        }
        List<CourseBean> CourseBeanList = new ArrayList<>();
        for (Result res : result) {
            for (Result.DayVos day : res.dayVos) {
                for (Result.DayVos.ScheduleItem lab : day.scheduleItemList) {
                    CourseBean labBean = lab.toCourseBean(getLessonCode);
                    CourseBeanList.add(labBean);
                }
            }
        }
        return CourseBeanList;
    }

    public static class Result {
        public int lessonBegin;
        public int lessonEnd;

        public List<DayVos> dayVos;

        public static class DayVos {
            public String date;
            public List<ScheduleItem> scheduleItemList;

            public static class ScheduleItem {
                public String id;
                public String schoolTime;
                public int weekNum;
                public int weekDay;
                public int lessonBegin;
                public int lessonEnd;
                public int publishFlag;
                public String subjectId;
                public String subjectName;
                public String relation;
                public String departId;
                public String campusId;
                public int minCount;
                public int conflictFlag;
                public int maxCount;
                public int selectCount;
                public int selectRule;
                public int flag;
                public String calendarId;
                public String createBy;
                public String createTime;
                public String labIds;
                public String labNames;
                public int labCount;
                public String teacherId;
                public String teacherName;
                public int stuCount;
                public boolean isLinkTheory;
                public String sameLessonFlag;
                public boolean scheduleModifyFlag;
                public String courseId;
                public String taskNum;
                public String taskName;

                public String description;

                public CourseBean toCourseBean(Map<String, String> getLessonCode) {
                    String LessonCode = "课号获取失败";
                    if (getLessonCode != null) {
                        LessonCode = getLessonCode.get(courseId);
                        if (LessonCode == null) {
                            LessonCode = "课号获取失败";
                        }
                    }
                    CourseBean courseBean = new CourseBean();
                    if (lessonBegin >= 13 || lessonEnd >=13) {
                        courseBean.setLab(LessonCode, taskName, subjectName, labNames, weekNum, weekDay, 5, 5, teacherName, description);
                    } else {
                        courseBean.setLab(LessonCode, taskName, subjectName, labNames, weekNum, weekDay, (lessonBegin >= 5 ? lessonBegin + 1 : lessonBegin), (lessonEnd >= 5 ? lessonEnd + 1 : lessonEnd), teacherName, description);
                    }
                    return courseBean;
                }
            }
        }
    }
}

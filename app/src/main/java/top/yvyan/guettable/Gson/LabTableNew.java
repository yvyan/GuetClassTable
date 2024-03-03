package top.yvyan.guettable.Gson;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;

public class LabTableNew {
    public boolean success;
    public List<Result> result;
    public List<CourseBean> toCourseBeans() {
        if (!success){
            return null;
        }
        List<CourseBean> CourseBeanList=new ArrayList<>();
        for (Result res:result) {
            for (Result.DayVos day : res.dayVos) {
                for (Result.DayVos.ScheduleItem lab : day.scheduleItemList) {
                    CourseBean labBean = lab.toCourseBean();
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

                public CourseBean toCourseBean() {
                    CourseBean courseBean = new CourseBean();
                    courseBean.setLab(subjectName,labNames,weekNum,weekDay,(lessonBegin>=5?lessonBegin+1:lessonBegin),(lessonEnd>=5?lessonEnd+1:lessonEnd),teacherName);
                    return courseBean;
                }
            }
        }
    }
}

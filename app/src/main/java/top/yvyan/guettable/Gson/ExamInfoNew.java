package top.yvyan.guettable.Gson;

import static java.lang.Integer.parseInt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.TimeUtil;

public class ExamInfoNew {
    static public class Course {
        public String nameZh;
    }

    public Course course;

    public int seatNo; // 座位号

    static public class LessonInfo {
        public String code; // 课号
        public String teacherAssignmentString; // 教师
    }

    public LessonInfo lesson;

    static public class ExamType {
        public String name;
    }

    public ExamType examType;
    public String examTime; // 考试时间

    static public class ExamPlace {
        public String arrangedExamPlace;
        public String courseNameStr;
    }

    public ExamPlace examPlace;

    public String room;

    public ExamBean toExamBean(GeneralData generalData) {
        try {
            String[] examDateTime = examTime.split(" ");
            String examDate = examDateTime[0];
            String examTime = examDateTime[1].replace("~", "-");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(examDate);
            int week = TimeUtil.calcWeekOffset(generalData.getStartTime(), date) + 1;
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date);
            int day = cal1.get(Calendar.DAY_OF_WEEK) - 1;
            if (day == 0) {
                day = 7;
            }
            String startTime = examTime.split("-")[0];
            String endTime = examTime.split("-")[1];
            int startHour = parseInt(startTime.split(":")[0]);
            int endHour = parseInt(endTime.split(":")[0]);
            String examRoom = room.trim() + "-" + seatNo;
            return new ExamBean(lesson.code, course.nameZh, lesson.teacherAssignmentString, week, day, TimeUtil.getCourseIndexByHour(startHour), TimeUtil.getCourseIndexByHour(endHour), examTime, examDate, examRoom, examType.name, "课程表开发者备注：目前相关教务系统功能仍未完善，该信息仅供参考，请以教务系统或老师安排为准。");
        } catch (Exception ignored) {
        }
        return null;
    }

}

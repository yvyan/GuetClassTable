package top.yvyan.guettable.data;

import com.zhuangfei.timetable.model.Schedule;

import java.util.List;

public class DetailClassData {
    private static List<Schedule> schedules;

    public static List<Schedule> getCourseBeans() {
        return schedules;
    }

    public static void setCourseBeans(List<Schedule> schedules) {
        DetailClassData.schedules = schedules;
    }
}

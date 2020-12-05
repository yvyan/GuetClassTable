package top.yvyan.guettable.util;

import com.zhuangfei.timetable.model.Schedule;

import java.util.Comparator;

public class ComparatorCourse implements Comparator<Schedule> {

    @Override
    public int compare(Schedule schedule, Schedule t1) {
        int flag = schedule.getWeekList().get(0) - t1.getWeekList().get(0);
        if (flag > 0) {
            return 1;
        } else if (flag < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}

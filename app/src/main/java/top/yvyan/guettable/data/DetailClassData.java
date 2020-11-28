package top.yvyan.guettable.data;

import com.zhuangfei.timetable.model.Schedule;

import java.util.List;

public class DetailClassData {
    private static DetailClassData detailClassData;

    private List<Schedule> schedules;

    private DetailClassData() {
    }

    public static DetailClassData newInstance() {
        if (detailClassData == null) {
            detailClassData = new DetailClassData();
        }
        return detailClassData;
    }

    public List<Schedule> getCourseBeans() {
        return schedules;
    }

    public void setCourseBeans(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}

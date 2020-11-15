package top.yvyan.guettable.data;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;

public class DayClassData {
    private static DayClassData dayClassData;

    private List<CourseBean> courseBeans;

    private DayClassData() {
    }

    public static DayClassData newInstance() {
        if (dayClassData == null) {
            dayClassData = new DayClassData();
        }
        return dayClassData;
    }

    public List<CourseBean> getCourseBeans() {
        return courseBeans;
    }

    public void setCourseBeans(List<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
    }
}

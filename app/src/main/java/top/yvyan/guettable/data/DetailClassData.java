package top.yvyan.guettable.data;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;

public class DetailClassData {
    private static DetailClassData detailClassData;

    private List<CourseBean> courseBeans;

    private DetailClassData() {
    }

    public static DetailClassData newInstance() {
        if (detailClassData == null) {
            detailClassData = new DetailClassData();
        }
        return detailClassData;
    }

    public List<CourseBean> getCourseBeans() {
        return courseBeans;
    }

    public void setCourseBeans(List<CourseBean> courseBeans) {
        this.courseBeans = courseBeans;
    }
}

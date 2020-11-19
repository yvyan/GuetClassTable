package top.yvyan.guettable.util;

import java.util.Comparator;

import top.yvyan.guettable.bean.CourseBean;

public class ComparatorCourse implements Comparator<CourseBean> {

    @Override
    public int compare(CourseBean courseBean, CourseBean t1) {
        int flag = courseBean.getWeekStart() - t1.getWeekStart();
        return flag;
    }
}

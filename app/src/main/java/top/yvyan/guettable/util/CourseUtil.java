package top.yvyan.guettable.util;

import com.zhuangfei.timetable.model.Schedule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.bean.PlannedCourseBean;

public class CourseUtil {
    /**
     * 合并考试安排(合并课号和考试时间相同的考试安排，教室合并显示)
     *
     * @param examBeans 源考试安排
     * @return 合并后的考试安排
     */
    public static List<ExamBean> combineExam(List<ExamBean> examBeans) {
            return examBeans;
    }

    /**
     * 去掉过期的考试安排
     *
     * @param examBeans 源考试安排
     * @return 未过期的考试安排
     */
    public static List<ExamBean> ridOfOutdatedExam(List<ExamBean> examBeans) {
        List<ExamBean> mExamBeans = new ArrayList<>();
        for (ExamBean examBean : examBeans) {
            if (TimeUtil.calcDayOffset(new Date(), examBean.getDate()) >= 0) {
                mExamBeans.add(examBean);
            }
        }
        return mExamBeans;
    }

    /**
     * 去掉必修课程中的限选和任选
     *
     * @param plannedCourseBeans 源计划课程
     * @return 去掉必修课程中的限选和任选的计划课程
     */
    public static List<PlannedCourseBean> ridRepeatScore(List<PlannedCourseBean> plannedCourseBeans) {
        List<PlannedCourseBean> mPlannedCourseBeans = new ArrayList<>();
        for (PlannedCourseBean plannedCourseBean : plannedCourseBeans) {
            if (!(plannedCourseBean.getTypeName().equals("必修课程") && plannedCourseBean.getCanRip())) {
                mPlannedCourseBeans.add(plannedCourseBean);
            }
        }
        return mPlannedCourseBeans;
    }

    /**
     * 用于课程按起始周数进行排序
     */
    public static class ComparatorCourse implements Comparator<Schedule> {

        @Override
        public int compare(Schedule schedule, Schedule t1) {
            int flag = schedule.getWeekList().get(0) - t1.getWeekList().get(0);
            return Integer.compare(flag, 0);
        }
    }

    public static class BeanAttributeUtil implements Comparator<BeanAttributeUtil.BeanAttribute> {

        public interface BeanAttribute {
            String getTerm();

            /**
             * 获得排序参考值，用于统一排序
             *
             * @return 排序参考值
             */
            long getOrder();
        }

        /**
         * 隐藏其它学期的内容
         *
         * @param Beans 实体类
         * @param term  学期
         * @return 筛选后的实体类
         */
        public static <T extends BeanAttribute> List<T> hideOtherTerm(List<T> Beans, String term) {
            List<T> mBeans = new ArrayList<>();
            for (T beans : Beans) {
                if (term.equals(beans.getTerm())) {
                    mBeans.add(beans);
                }
            }
            return mBeans;
        }

        /**
         * 用于排序
         */
        @Override
        public int compare(BeanAttribute beanAttribute, BeanAttribute t1) {
            long flag = beanAttribute.getOrder() - t1.getOrder();
            if (flag > 0) {
                return 1;
            } else if (flag < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

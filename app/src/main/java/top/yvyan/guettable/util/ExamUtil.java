package top.yvyan.guettable.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.bean.ExamBean;

public class ExamUtil {
    /**
     * 合并考试安排(合并课号和考试时间相同的考试安排，教室合并显示)
     * @param examBeans 源考试安排
     * @return          合并后的考试安排
     */
    public static List<ExamBean> combineExam(List<ExamBean> examBeans) {
        if (examBeans.size() >= 2) {
            List<ExamBean> mExamBeans = new ArrayList<>();
            ExamBean examBeanLast = new ExamBean(examBeans.get(0)), examBean;
            for (int i = 1; i < examBeans.size(); i++) {
                examBean = new ExamBean(examBeans.get(i));
                if (examBean.getNumber().equals(examBeanLast.getNumber()) /*&& examBean.getDate().equals(examBeanLast.getDate())*/ && examBean.getTime().equals(examBeanLast.getTime())) {
                    examBeanLast.setRoom(examBeanLast.getRoom() + "," + examBean.getRoom());
                } else {
                    mExamBeans.add(examBeanLast);
                    examBeanLast = examBean;
                }
            }
            mExamBeans.add(examBeanLast);
            return mExamBeans;
        } else {
            return examBeans;
        }
    }

    /**
     * 去掉过期的考试安排
     * @param examBeans 源考试安排
     * @return          未过期的考试安排
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
}

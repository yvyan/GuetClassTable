package top.yvyan.guettable.util;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.ExamBean;

public class ExamUtil {
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
}

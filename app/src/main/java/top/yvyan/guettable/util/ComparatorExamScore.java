package top.yvyan.guettable.util;

import java.util.Comparator;

import top.yvyan.guettable.bean.ExamScoreBean;

public class ComparatorExamScore implements Comparator<ExamScoreBean> {
    @Override
    public int compare(ExamScoreBean examScoreBean, ExamScoreBean t1) {
        int flag = Integer.parseInt(t1.getNumber()) - Integer.parseInt(examScoreBean.getNumber());
        if (flag > 0) {
            return 1;
        } else if (flag < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}

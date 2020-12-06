package top.yvyan.guettable.util;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.ExamScoreBean;

public class ExamScoreUtil {
    public static List<ExamScoreBean> hideOtherTermExamScore(List<ExamScoreBean> examScoreBeans, String term) {
        List<ExamScoreBean> mExamScoreBeans = new ArrayList<>();
        for (ExamScoreBean examScoreBean : examScoreBeans) {
            if (term.equals(examScoreBean.getTerm())) {
                mExamScoreBeans.add(examScoreBean);
            }
        }
        return mExamScoreBeans;
    }
}

package top.yvyan.guettable.util;

import java.util.Comparator;
import top.yvyan.guettable.bean.ExperimentScoreBean;

public class ComparatorExperimentScore implements Comparator<ExperimentScoreBean> {
    @Override
    public int compare(ExperimentScoreBean experimentScoreBean, ExperimentScoreBean t1) {
        int flag = Integer.parseInt(t1.getNumber()) - Integer.parseInt(experimentScoreBean.getNumber());
        if (flag > 0) {
            return 1;
        } else if (flag < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}

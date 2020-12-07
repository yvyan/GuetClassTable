package top.yvyan.guettable.util;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.BeanAttribute;

public class BeanHideUtil<T> {
    public static <T extends BeanAttribute> List<T> hideOtherTermExamScore(List<T> Beans, String term) {
        List<T> mBeans = new ArrayList<>();
        for (T beans : Beans) {
            if (term.equals(beans.getTerm())) {
                mBeans.add(beans);
            }
        }
        return mBeans;
    }
}

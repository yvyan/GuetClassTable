package top.yvyan.guettable.util;

import java.util.Comparator;

import top.yvyan.guettable.bean.BeanAttribute;

public class ComparatorBeanAttribute implements Comparator<BeanAttribute> {

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

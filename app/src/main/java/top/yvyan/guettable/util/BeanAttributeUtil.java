package top.yvyan.guettable.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BeanAttributeUtil implements Comparator<BeanAttributeUtil.BeanAttribute> {

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

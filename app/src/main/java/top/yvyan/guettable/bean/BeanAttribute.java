package top.yvyan.guettable.bean;

public interface BeanAttribute {
    String getTerm();

    /**
     * 获得排序参考值，用于统一排序
     *
     * @return 排序参考值
     */
    long getOrder();
}

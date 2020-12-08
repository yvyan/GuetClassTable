package top.yvyan.guettable.bean;

public interface BeanAttribute {
    public String getTerm();

    /**
     * 获得排序参考值，用于统一排序
     * @return 排序参考值
     */
    public long getOrder();
}

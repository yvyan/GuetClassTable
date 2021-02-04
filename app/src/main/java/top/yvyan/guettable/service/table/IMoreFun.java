package top.yvyan.guettable.service.table;

public interface IMoreFun {
    /**
     * 更新数据
     * @param cookie cookie
     * @return       5 : 正常
     *               1 : cookie失效
     */
    int updateData(String cookie);
    /**
     * state记录当前状态
     *  2 : 未登录
     *
     *  5 : 通用获取数据成功
     *
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     *
     * 91 : 登录状态检查
     * 92 : 正在登录
     * 93 : 正在更新
     */
    void updateView(String hint, int state);
}

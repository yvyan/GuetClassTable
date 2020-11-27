package top.yvyan.guettable.service;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

public class AutoUpdate {

    private static AutoUpdate autoUpdate;
    private Activity activity;

    private AccountData accountData;
    private ClassData classData;
    private GeneralData generalData;

    /**
     * state记录当前状态
     *  0 : 登录成功
     *  1 : 验证码错误
     *  2 : 密码错误
     *  3 : 网络错误/未知错误
     *  4 : 未登录
     *  5 : 就绪（默认）
     *
     *  6 : 更新理论课
     *  7 : 更新课内实验
     *  8 : 更新完成
     */
    private int state;

    private AutoUpdate(Activity activity) {
        this.activity = activity;
        accountData = AccountData.newInstance(activity);
        classData = ClassData.newInstance(activity);
        generalData = GeneralData.newInstance(activity);
        init();
    }

    private void init() {
        if (accountData.getIsLogin()) {
            updateView(5);
        } else {
            updateView(4);
        }
        updateView(state);
    }

    public static AutoUpdate newInstance(Activity activity) {
        if (autoUpdate == null) {
            autoUpdate = new AutoUpdate(activity);
        }
        return autoUpdate;
    }

    /**
     * 视图启动时调用
     */
    public void start() {
        // 判断时间间隔
        updateView();
        if (generalData.getLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getLastUpdateTime()), new Date(System.currentTimeMillis())) >= generalData.updateFrequency) {
            update();
        }
    }

    /**
     * 启动更新
     */
    public void update() {

        // 判断状态是否符合；合适的状态：就绪 网络错误 更新成功(点击更新)
        if (state == 5 || state == 3 || state == 8) {
            update_thread();
        }
    }

    /**
     * 首次登录成功后导入课表
     */
    public void firstLogin() {
        init();
        update();
    }

    public void logoff() {

    }

    public void updateView() {
        updateView(state);
    }

    private void updateView(int state) {
        this.state = state;
        String text = "error";
        switch (state) {
            case 5:
                text = "已登录(点击更新)";
                break;
            case 4:
                text = "点击登录";
                break;
            case 0:
                text = "登录成功";
                break;
            case 2:
                text = "密码错误";
                break;
            case 3:
                text = "网络错误(点击重试)";
                break;
            case 6:
                text = "正在更新理论课...";
                break;
            case 7:
                text = "正在更新实验课...";
                break;
            case 8:
                text = "更新成功(点击更新)";
                break;
        }
        final String out = text;
        activity.runOnUiThread(() -> {
            DayClassFragment.newInstance().updateText(out);
        });
    }

    private void update_thread() {
        new Thread(() -> {
            StringBuilder cookie_builder = new StringBuilder();
            // 自动登录
            updateView(
                    StaticService.autoLogin(
                        activity,
                        accountData.getUsername(),
                        accountData.getPassword(),
                        cookie_builder
                )
            );
            if (state == 0) {
                String cookie = cookie_builder.toString();
                List<CourseBean> courseBeans = new ArrayList<>();
                // 获取理论课
                updateView(6);
                List<CourseBean> getClass = StaticService.getClass(
                        activity,
                        cookie,
                        generalData.getTerm()
                        );
                if (getClass != null) {
                    courseBeans = getClass;
                    // 获取实验课
                    updateView(7);
                    List<CourseBean> getLab = StaticService.getLab(
                            activity,
                            cookie,
                            generalData.getTerm()
                    );
                    if (getLab != null) {
                        updateView(8);
                        courseBeans.addAll(getLab);
                        classData.setCourseBeans(courseBeans);
                        generalData.setLastUpdateTime(System.currentTimeMillis());
                        activity.runOnUiThread(() -> {
                            CourseTableFragment.newInstance().updateTable();
                            DayClassFragment.newInstance().updateView();
                            ToastUtil.showToast(activity, "更新成功");
                        });
                    } else {
                        updateView(3);
                    }
                } else {
                    updateView(3);
                }
            }
        }).start();
    }
}

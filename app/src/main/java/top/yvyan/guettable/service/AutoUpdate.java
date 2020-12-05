package top.yvyan.guettable.service;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.CookieData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
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
    private CookieData cookieData;

    private int state;

    private AutoUpdate(Activity activity) {
        this.activity = activity;
        accountData = AccountData.newInstance(activity);
        classData = ClassData.newInstance(activity);
        generalData = GeneralData.newInstance(activity);
        cookieData = CookieData.newInstance(activity);
        init();
    }

    private void init() {
        if (accountData.getIsLogin()) {
            updateView(0);
        } else {
            updateView(2);
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
        if (generalData.getLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getLastUpdateTime()), new Date()) >= generalData.updateFrequency) {
            update();
        }
    }

    /**
     * 启动更新
     */
    public void update() {
        init();
        // 判断状态是否符合；合适的状态：就绪 网络错误 更新成功(点击更新)
        if (state == 0 || state == -2 || state == 8) {
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

    public void updateView() {
        if (!accountData.getIsLogin()) {
            updateView(2);
        } else {
            updateView(state);
        }
    }

    /**
     * state记录当前状态
     *  0 : 登录成功
     *  1 : 登录失效
     *  2 : 未登录
     *
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     *
     *  6 : 正在更新理论课
     *  7 : 正在更新课内实验
     *  8 : 更新完成
     *
     * 21 : 理论课更新成功
     * 22 : 课内实验更新成功
     * 23 : 考试安排更新成功
     *
     */
    private void updateView(int state) {
        this.state = state;
        String text = "网络错误";
        switch (state) {
            case 0:
                text = "已登录(点击更新)";
                break;
            case 2:
                text = "去登录";
                break;
            case -1:
                text = "密码错误";
                break;
            case -2:
                text = "网络错误(点击重试)";
                break;
            case -3:
                text = "验证码识别失败";
            case 6:
                text = "正在更新理论课...";
                break;
            case 7:
                text = "正在更新实验课...";
                break;
            case 8:
                text = "更新成功(点击更新)";
                break;
            case 9:
                text = "正在更新考试安排...";
                break;
        }
        final String out = text;
        activity.runOnUiThread(() -> {
            try{
                DayClassFragment.newInstance().updateText(out);
            } catch (Exception e) {
            }
        });
    }

    /**
     * 自动更新线程
     */
    private void update_thread() {
        new Thread(() -> {
            // 刷新cookie
            updateView(cookieData.refresh());
            if (state == 0) {
                String cookie = cookieData.getCookie();
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
                    updateView(9);
                } else {
                    updateView(3);
                    return;
                }
                //获取考试安排
                List<ExamBean> examBeans = StaticService.getExam(
                        activity,
                        cookie,
                        generalData.getTerm()
                );
                if (examBeans != null) {
                    MoreDate.newInstance(activity).setExamBeans(examBeans);
                    updateView(7);
                } else {
                    updateView(3);
                    return;
                }
                //获取实验课
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
                    return;
                }
            }
        }).start();
    }
}

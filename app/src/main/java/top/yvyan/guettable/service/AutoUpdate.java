package top.yvyan.guettable.service;

import android.app.Activity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.CourseUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

import static top.yvyan.guettable.widget.WidgetUtil.notifyWidgetUpdate;

public class AutoUpdate {

    private final Activity activity;
    private final DayClassFragment fragment;

    private CourseTableFragment tableFragment;

    private final AccountData accountData;
    private final GeneralData generalData;
    private final TokenData tokenData;
    private final SettingData settingData;

    private int state;

    public AutoUpdate(DayClassFragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
        accountData = AccountData.newInstance(activity);
        generalData = GeneralData.newInstance(activity);
        tokenData = TokenData.newInstance(activity, this);
        settingData = SettingData.newInstance(activity);
        init();
    }

    public void setCourseTableFragment(CourseTableFragment courseTableFragment) {
        this.tableFragment = courseTableFragment;
    }

    private void init() {
        if (accountData.getIsLogin()) {
            updateView(0);
        } else {
            updateView(2);
        }
        updateView(state);
    }

    /**
     * 视图启动时调用
     */
    public void start() {
        init();
        // 判断时间间隔
        updateView();
        if (settingData.getIsRefresh()) {
            if (generalData.getLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getLastUpdateTime()), new Date()) >= settingData.getRefreshFrequency()) {
                update();
            }
        }
    }

    /**
     * 启动同步
     */
    public void update() {
        // 判断状态是否符合；合适的状态：就绪 登录失效 网络错误  同步成功(点击同步)
        if (state == 0 || state == -2 || state == -3 || state == 5 || state == -99) {
            update_thread();
        }
    }

    public void updateView() {
        if (!accountData.getIsLogin()) {
            updateView(2);
        } else {
            updateView(state);
        }
    }

    private void updateView(String text) {
        this.state = -99;
        activity.runOnUiThread(() -> {
            try {
                fragment.updateText(text);
            } catch (Exception ignored) {
            }
        });
    }

    private void updateView(int state) {
        this.state = state;
        if (state == 99) return;
        String text;
        switch (state) {
            case 0:
                text = "已登录(点击同步)";
                break;
            case 2:
                text = "去登录";
                break;
            case -1:
                text = "密码错误";
                break;
            case -2:
                text = "网络错误";
                break;
            case -3:
                text = "登录失效(点击重试)";
                break;
            case 91:
                text = "尝试同步课程表";
                break;
            case 92:
                text = "正在登录";
                break;
            case 93:
                text = "正在同步课程表";
                break;
            case 94:
                text = "正在同步考试安排";
                break;
            case 95:
                text = "正在同步课内实验";
                break;
            case 5:
                text = "同步成功";
                break;
            default:
                text = "未知错误";
                break;
        }
        final String out = text;
        activity.runOnUiThread(() -> {
            try {
                fragment.updateText(out);
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * 自动同步线程
     */
    private void update_thread() {
        new Thread(() -> {
            String cookie;
            try {
                if (accountData.getIsLogin()) {
                    updateView(91); //显示：尝试同步理论课
                    cookie = tokenData.getbkjwTestCookie();
                    List<CourseBean> getClass = StaticService.getClassNew(
                            activity,
                            cookie,
                            generalData.getTerm(),
                            GeneralData.isAutoTerm()
                    );
                    if (getClass != null) {
                        ScheduleData.setCourseBeans(getClass);
                    } else {
                        updateView(92);
                        state = tokenData.refresh();
                        if (state != 0) {
                            updateView(state);
                            return;
                        }
                        updateView(93); //显示：正在同步理论课

                        getClass = StaticService.getClassNew(
                                activity,
                                tokenData.getbkjwTestCookie(),
                                generalData.getTerm(),
                                GeneralData.isAutoTerm()
                        );
                        if (getClass != null) {
                            ScheduleData.setCourseBeans(getClass);
                        } else {
                            updateView("课程表同步失败");
                            return;
                        }
                    }
                    //小学期合并到第二学期
                    /*
                    String addTerm = generalData.getAddTerm();
                    if (!addTerm.isEmpty()) {
                        List<CourseBean> getAddClass = StaticService.getClass(
                                activity,
                                cookie,
                                addTerm
                        );
                        if (getAddClass != null && !getAddClass.isEmpty()) {
                            getClass.addAll(getAddClass);
                            ScheduleData.setCourseBeans(getClass);
                        }
                    }*/
                    //获取考试安排
                    updateView(94);
                    List<ExamBean> examBeans = StaticService.getExam(
                            activity,
                            tokenData.getBkjwCookie(),
                            generalData.getTerm()
                    );
                    if (examBeans != null) {
                        CourseUtil.BeanAttributeUtil beanAttributeUtil = new CourseUtil.BeanAttributeUtil();
                        Collections.sort(examBeans, beanAttributeUtil);
                        ScheduleData.setExamBeans(examBeans);
                    } else {
                        updateView("考试安排同步失败");
                        return;
                    }
                    updateView(5);
                    activity.runOnUiThread(() -> {
                        fragment.onStart();
                        notifyWidgetUpdate(activity);
                        ToastUtil.showToast(activity, "同步成功");
                    });
                    if (tableFragment != null) {
                        tableFragment.getActivity().runOnUiThread(()->{
                            try {
                                tableFragment.updateTable();
                            } catch (Exception e) {
                            }
                        });
                    }
                } else {
                    updateView(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> ToastUtil.showToast(activity, "同步失败，请联系开发者"));
                updateView(0);
            }
        }).start();
    }
}

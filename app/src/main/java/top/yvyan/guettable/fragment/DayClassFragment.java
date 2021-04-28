package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.umcrash.UMCrash;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import top.yvyan.guettable.MainActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.DayClassAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.GradesActivity;
import top.yvyan.guettable.service.table.AutoUpdate;
import top.yvyan.guettable.service.table.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private View view;
    private DayClassHandler handler = null;
    private Timer timer = null;
    private TimerTask timerTask = null;

    private TextView textView;
    private RecyclerView recyclerView;
    private View showExam;
    private TextView showExamDay;

    private AutoUpdate autoUpdate;

    private long[][] classTimeSection;
    private String times;

    private SingleSettingData singleSettingData;
    private AccountData accountData;
    private GeneralData generalData;
    private SettingData settingData;
    private ScheduleData scheduleData;

    public static DayClassFragment newInstance() {
        return new DayClassFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        times = UMRemoteConfig.getInstance().getConfigValue("classTime");
        handler = new DayClassHandler(Looper.getMainLooper(), new WeakReference<>(this));
        timer = new Timer();
        initMillTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_class, container, false);
        View tools = view.findViewById(R.id.day_class_tools);
        if (!SettingData.newInstance(getContext()).isShowTools()) {
            tools.setVisibility(View.GONE);
        }
        initData();
        View addStatus = view.findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getContext()));
        addStatus.setLayoutParams(lp);

        textView = view.findViewById(R.id.day_class_hint);
        textView.setOnClickListener(this);
        showExam = view.findViewById(R.id.day_show_exam);
        showExamDay = view.findViewById(R.id.day_show_exam_day);
        View testSchedule = view.findViewById(R.id.day_test_schedule);
        testSchedule.setOnClickListener(this);
        View urlBkjw = view.findViewById(R.id.day_url_bkjw);
        urlBkjw.setOnClickListener(this);
        View testScores = view.findViewById(R.id.day_test_scores);
        testScores.setOnClickListener(this);
        View credits = view.findViewById(R.id.day_credits);
        credits.setOnClickListener(this);

        autoUpdate = new AutoUpdate(this);
        if (accountData.getIsLogin()) {
            autoUpdate.start();
        }
        recyclerView = view.findViewById(R.id.day_class_detail_recycleView);
        return view;
    }

    private void initData() {
        singleSettingData = SingleSettingData.newInstance(getActivity());
        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());
        settingData = SettingData.newInstance(getActivity());
        scheduleData = ScheduleData.newInstance(getActivity());
    }

    /**
     * 更新日课表视图
     */
    @SuppressLint("SetTextI18n")
    public void updateView(int... order) {
        List<Schedule> allClass = getData();
        List<Schedule> todayList, tomorrowList;
        if (allClass != null) {
            todayList = ScheduleSupport.getHaveSubjectsWithDay(
                    allClass,
                    generalData.getWeek(),
                    TimeUtil.getDay()
            );
            tomorrowList = ScheduleSupport.getHaveSubjectsWithDay(
                    allClass,
                    TimeUtil.getNextDayWeek(generalData.getWeek()),
                    TimeUtil.getNextDay()
            );
        } else {
            todayList = new ArrayList<>();
            tomorrowList = new ArrayList<>();
        }
        DayClassAdapter dayClassAdapter;
        if (order.length == 0) {
            dayClassAdapter = new DayClassAdapter(getContext(), todayList, tomorrowList);
        } else {
            dayClassAdapter = new DayClassAdapter(getContext(), todayList, tomorrowList, order[0]);
        }
        dayClassAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dayClassAdapter);

        //更新考试剩余时间信息
        try {
            List<ExamBean> examBeans = scheduleData.getExamBeans();
            examBeans = ExamUtil.combineExam(examBeans);
            examBeans = ExamUtil.ridOfOutdatedExam(examBeans);
            if (examBeans.size() != 0) {
                showExam.setVisibility(View.VISIBLE);

                int n = TimeUtil.calcDayOffset(new Date(), examBeans.get(0).getDate());
                if (n >= 1) {
                    showExamDay.setText("您" + n + "天后有考试");
                } else {
                    showExamDay.setText("您今天有考试");
                }
            } else {
                showExam.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            UMCrash.generateCustomLog(e, "Day.showExamDay");
            showExam.setVisibility(View.GONE);
        }
    }

    private void setBackground(boolean setBackground) {
        View addStatus = view.findViewById(R.id.add_status);
        View titleBar = view.findViewById(R.id.title_bar);
        if (setBackground) {
            titleBar.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            addStatus.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
        } else {
            titleBar.getBackground().setAlpha(255);
            addStatus.getBackground().setAlpha(255);
        }
    }

    /**
     * 用户点击状态文字时的响应
     *
     * @param view 视图
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        switch (view.getId()) {
            case R.id.day_class_hint:
                if ("去登录".contentEquals(textView.getText())) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null) {
                        mainActivity.onClick(3);
                    }
                    return;
                }
                autoUpdate.update();
                AppUtil.reportFunc(getContext(), "手动同步");
                break;
            case R.id.day_test_schedule:
                intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.day_url_bkjw:
                CommFunc.noLoginWebBKJW(getActivity());
                break;
            case R.id.day_test_scores:
                intent = new Intent(getContext(), ExamScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.day_credits:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), GradesActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                ToastUtil.showToast(getContext(), "敬请期待！");
        }
    }

    public void updateText(String text) {
        textView.setText(text);
    }

    /**
     * 获取List<Schedule>类型的课表数据
     *
     * @return List<Schedule>类型的课表数据
     */
    private List<Schedule> getData() {
        List<Schedule> list;
        if (!ScheduleData.newInstance(getActivity()).getCourseBeans().isEmpty()) {
            list = ScheduleSupport.transform(scheduleData.getCourseBeans());
        } else {
            list = new ArrayList<>();
        }
        for (CourseBean courseBean : scheduleData.getUserCourseBeans()) {
            list.add(courseBean.getSchedule());
        }
        if (settingData.getShowLibOnTable()) {
            List<Schedule> labList = ScheduleSupport.transform(scheduleData.getLibBeans());
            list.addAll(labList);
        }
        if (settingData.getShowExamOnTable()) {
            for (ExamBean examBean : ExamUtil.combineExam(scheduleData.getExamBeans())) {
                if (examBean != null && examBean.getWeek() != 0) {
                    list.add(examBean.getSchedule());
                }
            }
        }
        return list;
    }

    //  定时任务，一分钟检查一次时间
    class timeTask extends TimerTask {
        @Override
        public void run() {
            if (handler != null) {
                int currentOrder;
                if ((currentOrder = getCurrentOrder()) != -1) {
                    Message msg = handler.obtainMessage();
                    msg.arg1 = currentOrder;
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }
    }

    //  没有打开Fragment的时候停止计时
    @Override
    public void onPause() {
        super.onPause();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //      Timer回收
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    //  定时任务周期半分钟
    @Override
    public void onStart() {
        try {
            super.onStart();
            setBackground(BackgroundUtil.isSetBackground(getContext()));
            initData();
            autoUpdate.updateView();
            timerTask = new timeTask();
            timer.schedule(timerTask, 30000, 30000);
            int order = -1;
            try {
                order = getCurrentOrder();
            } catch (Exception e) {
                UMCrash.generateCustomLog(e, "getCurrentOrder");
            }
            if (order != -1) {
                updateView(order);
            } else {
                updateView();
            }
        } catch (Exception e) {
            UMCrash.generateCustomLog(e, "DayClassFragmentStart");
        }
    }

    //  接收定时器通信的Handler，在其中调用updateView()来刷新日课表
    static class DayClassHandler extends Handler {
        WeakReference<DayClassFragment> weakReference;

        public DayClassHandler(@NonNull Looper looper, WeakReference<DayClassFragment> weakReference) {
            super(looper);
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                weakReference.get().updateView(msg.arg1);
            } else if (msg.what == 2) {
                weakReference.get().updateView();
            }
        }

    }

    //  获取当前正在进行的课程节次，若没有在上课，返回-1
    int getCurrentOrder() {
        long mills = System.currentTimeMillis();
        int current = -1, i;
        if (classTimeSection == null) {
            return -1;
        }
        for (i = 0; i < 7; i++) {
            if (mills >= classTimeSection[i][0] && mills <= classTimeSection[i][1]) {
                break;
            }
        }
        switch (i) {
            case 0:
                current = 13;
                break;
            case 1:
                current = 1;
                break;
            case 2:
                current = 3;
                break;
            case 3:
                current = 5;
                break;
            case 4:
                current = 7;
                break;
            case 5:
                current = 9;
                break;
            case 6:
                current = 11;
                break;
            default:
        }
        return current;
    }

    /**
     * 通过在线参数获取7个课程时间段的小时与分钟数
     * 作为参数为日历对象设置时间后，计算出对应时间段毫秒的上下限
     * 之后直接使用当前毫秒数所在的区间段来判断是否上课即可
     */
    void initMillTime() {
        try {
            String[] classTime = times.replaceAll(" ", "").split(";");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            classTimeSection = new long[7][2];
            String[][] temp = new String[2][2];   // 1列为小时，2列为分钟
            for (int i = 0; i < 7; i++) {
                int index = classTime[i].indexOf("~");
                temp[0] = classTime[i].substring(0, index).split(":");
                temp[1] = classTime[i].substring(index + 1).split(":");
                for (int j = 0; j < 2; j++) {
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[j][0].trim()));
                    cal.set(Calendar.MINUTE, Integer.parseInt(temp[j][1].trim()));
                    classTimeSection[i][j] = cal.getTimeInMillis();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
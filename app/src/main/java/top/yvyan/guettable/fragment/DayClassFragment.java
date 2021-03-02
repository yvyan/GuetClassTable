package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umeng.umcrash.UMCrash;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;
import top.yvyan.guettable.util.UrlReplaceUtil;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static DayClassFragment dayClassFragment;

    private View view;

    private TextView textView;
    private RecyclerView recyclerView;
    private View showExam;
    private TextView showExamDay;

    private AutoUpdate autoUpdate;
    private OnButtonClick onButtonClick;

    private SingleSettingData singleSettingData;
    private AccountData accountData;
    private GeneralData generalData;
    private SettingData settingData;
    private ScheduleData scheduleData;

    public DayClassFragment() {
        // Required empty public constructor
    }

    public static DayClassFragment newInstance() {
        if (dayClassFragment == null) {
            dayClassFragment = new DayClassFragment();
        }
        return dayClassFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dayClassFragment = this;
        view = inflater.inflate(R.layout.fragment_day_class, container, false);

        View tools = view.findViewById(R.id.day_class_tools);
        if (!SettingData.newInstance(getContext()).isShowTools()) {
            tools.setVisibility(View.GONE);
        }

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

        initData();

        autoUpdate = AutoUpdate.newInstance(getActivity());
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
    public void updateView() {
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
        DayClassAdapter dayClassAdapter = new DayClassAdapter(getContext(), todayList, tomorrowList);
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
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
        } else {
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.getBackground().setAlpha(255);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setBackground(BackgroundUtil.isSetBackground(getContext()));
        initData();
        autoUpdate.updateView();
        updateView();
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
        Uri uri;
        switch (view.getId()) {
            case R.id.day_class_hint:
                if ("去登录".contentEquals(textView.getText())) {
                    if (onButtonClick != null) {
                        onButtonClick.onClick(3);
                    }
                    return;
                }
                autoUpdate.update();
                break;
            case R.id.day_test_schedule:
                intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.day_url_bkjw:
                uri = Uri.parse(UrlReplaceUtil.getUrlByInternational(generalData.isInternational(), getContext().getResources().getString(R.string.url_bkjw)));
                webIntent.setData(uri);
                startActivity(webIntent);
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

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }
}
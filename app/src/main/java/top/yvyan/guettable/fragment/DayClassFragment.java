package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.DayClassAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.GradesActivity;
import top.yvyan.guettable.service.table.AutoUpdate;
import top.yvyan.guettable.service.app.UpdateApp;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.TextDialog;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;
import top.yvyan.guettable.util.UrlReplaceUtil;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private static DayClassFragment dayClassFragment;
    private static final String TAG = "DayClassFragment";

    private TextView textView;
    private RecyclerView recyclerView;
    private View showExam;
    private TextView showExamDay;

    private AutoUpdate autoUpdate;
    private OnButtonClick onButtonClick;

    private AccountData accountData;
    private GeneralData generalData;
    private SettingData settingData;
    private ScheduleData scheduleData;

    public DayClassFragment() {
        // Required empty public constructor
    }

    public static DayClassFragment newInstance() {
        if (dayClassFragment == null) {
            DayClassFragment fragment = new DayClassFragment();
            dayClassFragment = fragment;
        }
        return dayClassFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dayClassFragment = this;
        View view = inflater.inflate(R.layout.fragment_day_class, container, false);

        View tools = view.findViewById(R.id.day_class_tools);
        if (!SettingData.newInstance(getContext()).isShowTools()) {
            tools.setVisibility(View.GONE);
        }

        View addStatus = view.findViewById(R.id.add_status);
        //注意这里，到底是用ViewGroup还是用LinearLayout或者是FrameLayout，主要是看你这个EditTex
        //控件所在的父控件是啥布局，如果是LinearLayout，那么这里就要改成LinearLayout.LayoutParams
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
        // 检查更新
        UpdateApp.checkUpdate(getContext(), 1);

        autoUpdate = AutoUpdate.newInstance(getActivity());
        if (accountData.getIsLogin()) {
            autoUpdate.start();
        }
        recyclerView = view.findViewById(R.id.day_class_detail_recycleView);

        return view;
    }

    private void initData() {
        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());
        settingData = SettingData.newInstance(getActivity());
        scheduleData = ScheduleData.newInstance(getActivity());
    }

    /**
     * 更新日课表视图
     */
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        initData();
        autoUpdate.updateView();
        updateView();
    }

    /**
     * 用户点击状态文字时的响应
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        Uri uri;
        switch (view.getId()) {
            case R.id.day_class_hint:
                if ("去登录".equals(textView.getText())) {
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
                    TextDialog.showScanNumberDialog(getContext(), "国际学院教务系统暂无此功能");
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
     * @return List<Schedule>类型的课表数据
     */
    private List<Schedule> getData() {
        List<Schedule> list;
        if(!ScheduleData.newInstance(getActivity()).getCourseBeans().isEmpty()) {
            list = ScheduleSupport.transform(scheduleData.getCourseBeans());
        } else {
            list = new ArrayList<>();
        }
        if (settingData.getShowLibOnTable()) {
            List<Schedule> labList = ScheduleSupport.transform(scheduleData.getLibBeans());
            list.addAll(labList);
        }
        if (settingData.getShowExamOnTable() && !"2019-2020_2".equals(generalData.getTerm())) {
            for (ExamBean examBean : ExamUtil.combineExam(scheduleData.getExamBeans())) {
                if (examBean != null) {
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
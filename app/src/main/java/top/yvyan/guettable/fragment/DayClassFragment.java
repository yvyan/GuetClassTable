package top.yvyan.guettable.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.DayClassAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.AutoUpdate;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.TimeUtil;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private static DayClassFragment dayClassFragment;

    private ViewPager viewPager;
    private View view;
    private TextView textView;
    private RecyclerView recyclerView;

    private DayClassAdapter dayClassAdapter;
    private AutoUpdate autoUpdate;
    private OnButtonClick onButtonClick;

    private AccountData accountData;
    private GeneralData generalData;

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
        view = inflater.inflate(R.layout.fragment_day_class, container, false);

        Log.d("test:", "create");

        viewPager = view.findViewById(R.id.vp);
        textView = view.findViewById(R.id.day_class_hint);
        textView.setOnClickListener(this);
        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());

        autoUpdate = AutoUpdate.newInstance(getActivity());
        if (accountData.getIsLogin()) {
            autoUpdate.start();
        }

        recyclerView = view.findViewById(R.id.day_class_detail_recycleView);
        updateView();

        return view;
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
        dayClassAdapter = new DayClassAdapter(getContext(), todayList, tomorrowList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dayClassAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        autoUpdate.updateView();
        Log.d("test:", "start");
    }

    /**
     * 用户点击状态文字时的响应
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        if ("去登录".equals(textView.getText())) {
            if (onButtonClick != null) {
                onButtonClick.onClick(3);
            }
            return;
        }
        autoUpdate.update();
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
        if(!ClassData.newInstance(getActivity()).getCourseBeans().isEmpty()) {
            list = ScheduleSupport.transform(ClassData.newInstance(getActivity()).getCourseBeans());
            list = ScheduleSupport.getColorReflect(list);//分配颜色
        } else {
            list = new ArrayList<>();
        }
        for (ExamBean examBean : ExamUtil.combineExam(MoreDate.newInstance(getActivity()).getExamBeans())) {
            if (examBean != null) {
                list.add(examBean.getSchedule());
            }
        }
        return list;
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

}
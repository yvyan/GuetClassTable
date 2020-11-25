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

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.AutoUpdate;
import top.yvyan.guettable.util.TimeUtil;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private static DayClassFragment dayClassFragment;

    private View view;
    private TextView textView;
    private RecyclerView recyclerView;
    private ClassDetailAdapter classDetailAdapter;
    private AccountData accountData;
    private GeneralData generalData;
    private AutoUpdate autoUpdate;

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

        textView = view.findViewById(R.id.day_class_test);
        textView.setOnClickListener(this);
        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());

        autoUpdate = AutoUpdate.newInstance(getActivity());
        autoUpdate.start();

        recyclerView = view.findViewById(R.id.day_class_detail_recycleView);
        updateView();

        return view;
    }

    /**
     * 更新日课表视图
     */
    public void updateView() {
        List<Schedule> tmpList = ScheduleSupport.getHaveSubjectsWithDay(
                getData(), GeneralData.newInstance(getActivity()).getWeek(), TimeUtil.getDay());
        List<CourseBean> courseBeans = new ArrayList<>();
        for (Schedule schedule : tmpList) {
            CourseBean courseBean = new CourseBean();
            courseBean.setFromSchedule(schedule);
            courseBeans.add(courseBean);
        }
        classDetailAdapter = new ClassDetailAdapter(courseBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(classDetailAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        autoUpdate.updateView();
        Log.d("test:", "start");
        //updateUser();
    }

    /**
     * 用户点击状态文字时的响应
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        autoUpdate.update();
    }

    private void updateUser() {
        if (accountData.getIsLogin()) {
            textView.setText("已登录");
        } else {
            textView.setText("请登录");
        }
    }

    public void updateText(String text) {
        textView.setText(text);
        Log.d("test:", "updateText");
    }

    /**
     * 获取List<Schedule>类型的课表数据
     * @return List<Schedule>类型的课表数据
     */
    private List<Schedule> getData() {
        List<Schedule> list;
        if(ClassData.newInstance(getActivity()).getCourseBeans() != null) {
            list = ScheduleSupport.transform(ClassData.newInstance(getActivity()).getCourseBeans());
            list = ScheduleSupport.getColorReflect(list);//分配颜色
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

}
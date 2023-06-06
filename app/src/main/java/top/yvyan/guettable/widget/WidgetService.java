package top.yvyan.guettable.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.util.CourseUtil;
import top.yvyan.guettable.util.TimeUtil;

public class WidgetService extends RemoteViewsService {

    private GeneralData generalData;
    private SettingData settingData;
    private static final String TAG = "RemoteViewsService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory");
        List<Schedule> todayList = null;
        try {
            generalData = GeneralData.newInstance(getApplicationContext());
            settingData = SettingData.newInstance(getApplicationContext());
            todayList = new ArrayList<>(getTodayList());
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return new WidgetServiceFactory(getApplicationContext(), todayList);
    }

    // 日课表的获取当日课程方法
    private List<Schedule> getTodayList() {
        List<Schedule> list;
        if (!ScheduleData.getCourseBeans().isEmpty()) {
            list = ScheduleSupport.transform(ScheduleData.getCourseBeans());
        } else {
            list = new ArrayList<>();
        }
        for (CourseBean courseBean : ScheduleData.getUserCourseBeans()) {
            list.add(courseBean.getSchedule());
        }
        if (settingData.getShowLibOnTable()) {
            List<Schedule> labList = ScheduleSupport.transform(ScheduleData.getLibBeans());
            list.addAll(labList);
        }
        if (settingData.getShowExamOnTable()) {
            for (ExamBean examBean : CourseUtil.combineExam(ScheduleData.getExamBeans())) {
                if (examBean != null && examBean.getWeek() != 0) {
                    list.add(examBean.getSchedule());
                }
            }
        }
        return ScheduleSupport.getHaveSubjectsWithDay(
                list,
                generalData.getWeek(),
                TimeUtil.getDay()
        );
    }
}

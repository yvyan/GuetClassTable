package top.yvyan.guettable.widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.util.ExamUtil;
import top.yvyan.guettable.util.TimeUtil;

// 这里相当于ListView的适配器
public class WidgetServiceFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Schedule> dataList;
    private GeneralData generalData;
    private SettingData settingData;
    private ScheduleData scheduleData;
    private static final String TAG = "WidgetServiceFactory";


    public WidgetServiceFactory(Context context, List<Schedule> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, ": onDataSetChanged");
        if (generalData == null) {
            generalData = GeneralData.newInstance(context);
        }
        if (settingData == null) {
            settingData = SettingData.newInstance(context);
        }
        if (scheduleData == null) {
            scheduleData = ScheduleData.newInstance(context);
        }
        dataList = getTodayList();
    }

    @Override
    public void onDestroy() {
        dataList.clear();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < 0 || position >= dataList.size())
            return null;
        Schedule schedule = dataList.get(position);
        final RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.course_widget_cardview);
        CourseBean courseBean = new CourseBean();
        courseBean.setFromSchedule(schedule);
        rv.setTextViewText(R.id.widget_tv_course_name, schedule.getName());
        rv.setTextViewText(R.id.widget_tv_course_number, String.valueOf(courseBean.getTime()));
        if (schedule.getRoom() != null) {
            if (schedule.getRoom().contains("*")) {
                rv.setTextViewText(R.id.widget_tv_course_room, schedule.getRoom());
            } else {
                rv.setTextViewText(R.id.widget_tv_course_room, schedule.getRoom().concat("*"));
            }
        }
        rv.setTextViewText(R.id.widget_tv_course_teacher, schedule.getTeacher());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    // 日课表的获取当日课程方法
    private List<Schedule> getTodayList() {
        List<Schedule> list;
        if (!scheduleData.getCourseBeans().isEmpty()) {
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
        return ScheduleSupport.getHaveSubjectsWithDay(
                list,
                generalData.getWeek(),
                TimeUtil.getDay()
        );
    }
}

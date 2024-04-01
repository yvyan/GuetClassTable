package top.yvyan.guettable.adapter;

import com.zhuangfei.timetable.listener.OnDateBuildAapter;

import java.util.Calendar;
import java.util.Locale;

public class DateBuildAdapter extends OnDateBuildAapter {
    @Override
    public void onHighLight() {
        initDateBackground();

        //获取周几，1->7
        Calendar now = Calendar.getInstance();
        int weekDay = now.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 0) {
            weekDay = 7;
        }
        activeDateBackground(weekDay);
    }
}
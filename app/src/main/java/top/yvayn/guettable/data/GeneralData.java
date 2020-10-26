package top.yvayn.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import top.yvayn.guettable.util.TimeUtil;

public class GeneralData {
    private static GeneralData generalData;
    private static final String SHP_NAME = "GeneralData";
    private static final String WEEK = "week";
    private static final String TIME = "time";
    SharedPreferences sharedPreferences;

    private int week;
    private long time;

    private GeneralData(Activity activity) {
        sharedPreferences = activity.getApplication().getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    public static GeneralData newInstance(Activity activity) {
        if (generalData == null) {
            generalData = new GeneralData(activity);
        }
        return generalData;
    }

    public int getWeek() {
        int err = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
        return week + err;
    }

    public void setWeek(int week) {
        this.time = System.currentTimeMillis();
        this.week = week;
        save();
    }

    private void load() {
        week = sharedPreferences.getInt(WEEK, 1);
        time = sharedPreferences.getLong(TIME, System.currentTimeMillis());
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME, time);
        editor.putInt(WEEK, week);
        editor.apply();
    }
}

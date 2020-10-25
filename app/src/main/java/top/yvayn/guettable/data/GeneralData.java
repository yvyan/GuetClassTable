package top.yvayn.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GeneralData {
    private static GeneralData generalData;
    private static final String SHP_NAME = "GeneralData";
    private static final String WEEK = "week";
    SharedPreferences sharedPreferences;

    private int week;

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
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
        save();
    }

    private void load() {
        week = sharedPreferences.getInt(WEEK, 1);
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WEEK, week);
        editor.apply();
    }

}

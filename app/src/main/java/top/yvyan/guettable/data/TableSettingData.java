package top.yvyan.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class TableSettingData {
    private static TableSettingData tableSettingData;
    private static final String SHP_NAME = "tableSettingData";
    private static final String HIDE_OTHER_WEEK = "hideOtherWeek";
    SharedPreferences sharedPreferences;

    private boolean hideOtherWeek;

    private TableSettingData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        hideOtherWeek = sharedPreferences.getBoolean(HIDE_OTHER_WEEK, false);
    }

    public static TableSettingData newInstance(Context context) {
        if (tableSettingData == null) {
            tableSettingData = new TableSettingData(context);
        }
        return tableSettingData;
    }

    public boolean isHideOtherWeek() {
        return hideOtherWeek;
    }

    public void setHideOtherWeek(boolean hideOtherWeek) {
        this.hideOtherWeek = hideOtherWeek;
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HIDE_OTHER_WEEK, hideOtherWeek);
        editor.apply();
    }
}

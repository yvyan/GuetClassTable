package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import top.yvyan.guettable.fragment.SettingFragment;

public class SettingData {
    private static SettingData settingData;
    private Context context;
    private SharedPreferences sharedPreferences;

    private SettingData(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingData newInstance(Context context) {
        if (settingData == null) {
            settingData = new SettingData(context);
        }
        return settingData;
    }

    public boolean getIsRefresh() {
        return sharedPreferences.getBoolean(SettingFragment.REFRESH_DATA, true);
    }

    public int getRefreshFrequency() {
        return Integer.parseInt(sharedPreferences.getString(SettingFragment.REFRESH_DATA_FREQUENCY, "3"));
    }

    public boolean getShowExamOnTable() {
        return sharedPreferences.getBoolean(SettingFragment.SHOW_EXAM_ON_TABLE, true);
    }

    public int getClassLength() {
        return Integer.parseInt(sharedPreferences.getString(SettingFragment.CLASS_LENGTH, "60"));
    }

    public boolean getUpdateLab() {
        return sharedPreferences.getBoolean(SettingFragment.UPDATE_LAB, true);
    }

}

package top.yvyan.guettable.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import top.yvyan.guettable.R;

public class SettingFragment extends PreferenceFragment {

    public static final String BASE_SETTINGS = "basic_settings";
    public static final String REFRESH_DATA = "refresh_data";
    public static final String REFRESH_DATA_FREQUENCY = "refresh_data_frequency";
    public static final String SHOW_EXAM_ON_TABLE = "show_exam_on_table";
    public static final String SHOW_TOOLS_ON_DAY_CLASS = "show_tools_on_day_class";
    public static final String APP_CHECK_UPDATE = "app_check_update";
    public static final String CLASS_LENGTH = "class_length";


    private PreferenceCategory basicSettings;
    private SwitchPreference refreshData;
    private ListPreference refreshDataFrequency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

        basicSettings = (PreferenceCategory) findPreference(BASE_SETTINGS);

        refreshData = (SwitchPreference) findPreference(REFRESH_DATA);
        refreshData.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean)newValue) {
                basicSettings.addPreference(refreshDataFrequency);
            } else {
                basicSettings.removePreference(refreshDataFrequency);
            }
            return true;
        });

        refreshDataFrequency = (ListPreference) findPreference(REFRESH_DATA_FREQUENCY);
        refreshDataFrequency.setSummary("刷新频率：" + refreshDataFrequency.getValue() + "天");
        refreshDataFrequency.setOnPreferenceChangeListener((preference, newValue) -> {
            refreshDataFrequency.setSummary("刷新频率：" + newValue.toString() + "天");
            return true;
        });
    }
}
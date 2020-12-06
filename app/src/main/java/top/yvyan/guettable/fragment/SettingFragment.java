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


    private PreferenceCategory basicSettings;
    private SwitchPreference refreshData;
    private ListPreference refreshDataFrequency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);

        basicSettings = (PreferenceCategory) findPreference(BASE_SETTINGS);
        refreshData = (SwitchPreference) findPreference(REFRESH_DATA);
        refreshDataFrequency = (ListPreference) findPreference(REFRESH_DATA_FREQUENCY);
        refreshDataFrequency.setSummary("刷新频率：" + refreshDataFrequency.getValue() + "天");
        refreshData.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean)newValue) {
                basicSettings.addPreference(refreshDataFrequency);
            } else {
                basicSettings.removePreference(refreshDataFrequency);
            }
            return true;
        });

        refreshDataFrequency.setOnPreferenceChangeListener((preference, newValue) -> {
            refreshDataFrequency.setSummary("刷新频率：" + newValue.toString() + "天");
            return true;
        });

    }
}
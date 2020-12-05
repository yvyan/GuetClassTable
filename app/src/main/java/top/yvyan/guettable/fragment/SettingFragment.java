package top.yvyan.guettable.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.Toast;

import top.yvyan.guettable.R;
import top.yvyan.guettable.util.ToastUtil;

public class SettingFragment extends PreferenceFragment {
    private PreferenceCategory basic_settings;
    private SwitchPreference  refresh_data;
    private ListPreference list_day;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
        basic_settings = (PreferenceCategory) findPreference("basic_settings");
        refresh_data = (SwitchPreference) findPreference("refresh_data");
        list_day = (ListPreference) findPreference("list_day");


        refresh_data.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((boolean)newValue) {
                    basic_settings.addPreference(list_day);
                    ToastUtil.showLongToast(getContext(),"已开启");
                } else {
                    basic_settings.removePreference(list_day);
                    ToastUtil.showLongToast(getContext(),"已关闭");
                }
                return true;
            }
        });

        list_day.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                list_day.setSummary("刷新频率："+newValue.toString());
                return true;
            }
        });

    }


    //获取到配置信息
    public void getSp(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean switchRealTime = sp.getBoolean("refresh_data",false);

    }

    //配置全局监听，当改变的时候触发
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//            return super.onPreferenceTreeClick(preferenceScreen, preference);
        /*
        SharedPreferences sp = preference.getSharedPreferences();
        boolean switchRealTime = sp.getBoolean("",false);
        */
        return true;
    }
}
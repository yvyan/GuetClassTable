package top.yvyan.guettable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends PreferenceActivity {
    ListPreference listPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting);
        SettingFragment settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, settingFragment)
                .commit();
    }

    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_setting);
        }
/*

        //获取到配置信息
        public void getSp(){
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean switchRealTime = sp.getBoolean("switch_preference_1",false);
        }
        //配置监听，当改变的时候触发
        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//            return super.onPreferenceTreeClick(preferenceScreen, preference);
            SharedPreferences sp = preference.getSharedPreferences();
            boolean switchRealTime = sp.getBoolean("switch_preference_1",false);
            return true;
        }
*/
    }
}
package top.yvyan.guettable;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import top.yvyan.guettable.fragment.SettingFragment;


public class SettingActivity extends PreferenceActivity {
    ListPreference listPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingFragment settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, settingFragment)
                .commit();
    }

}
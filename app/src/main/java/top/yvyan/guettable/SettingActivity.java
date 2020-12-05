package top.yvyan.guettable;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.widget.Button;
import android.widget.ImageView;

import top.yvyan.guettable.fragment.SettingFragment;


public class SettingActivity extends PreferenceActivity {
    ListPreference listPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting);
        ImageView back= (ImageView) findViewById(R.id.setting_back);
        back.setOnClickListener(view -> {
            finish();
        });

        SettingFragment settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .add(android.R.id.content, settingFragment)
                .commit();

    }

}
package top.yvyan.guettable;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ImageView;

import top.yvyan.guettable.fragment.SettingFragment;


public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_setting);
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
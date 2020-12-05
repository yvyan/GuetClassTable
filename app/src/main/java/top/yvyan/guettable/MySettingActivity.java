package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import top.yvyan.guettable.fragment.SettingFragment;

public class MySettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_setting);
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

    public void doBack(View v) {
        finish();
    }
}
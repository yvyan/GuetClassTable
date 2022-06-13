package top.yvyan.guettable.moreFun;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.cconfig.UMRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;

public class QQGroupActivity extends AppCompatActivity {
    private String setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(this);
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_q_q_group);

        ConstraintLayout header = findViewById(R.id.func_base_constraintLayout);
        header.getBackground().setAlpha(255);

        BackgroundUtil.setFullAlphaStatus(this);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.moreFun_qqGroup));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_qqGroup));

        setting = UMRemoteConfig.getInstance().getConfigValue("groupSetting");
    }

    public void doBack(View view) {
        finish();
    }

    public void report(Context context, String funcName) {
        Map<String, Object> funcMap = new HashMap<>();
        funcMap.put("name", funcName);
        MobclickAgent.onEventObject(context, "addGroup", funcMap);
    }

    public void addGroup(String name, String key, int num) {
        report(this, name);
        if (setting.charAt(num) == '1') {
            DialogUtil.showTextDialog(this, "该群因违反协议，已被禁止！");
        } else {
            AppUtil.joinQQGroup(key, this);
        }
    }

    public void guetClassTable(View view) {
        addGroup(getResources().getString(R.string.text_guet_class_table), getResources().getString(R.string.key_guet_class_table), 0);
    }

    public void guetClassTable2(View view) {
        addGroup(getResources().getString(R.string.text_guet_class_table2), getResources().getString(R.string.key_guet_class_table2), 4);
    }

    public void guetFind3(View view) {
        addGroup(getResources().getString(R.string.text_guet_find_3), getResources().getString(R.string.key_guet_find_3), 1);
    }

    public void stdioMusic(View view) {
        addGroup(getResources().getString(R.string.text_stdio_music), getResources().getString(R.string.key_stdio_music), 3);
    }

}
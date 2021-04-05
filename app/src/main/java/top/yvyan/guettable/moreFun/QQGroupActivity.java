package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.cconfig.UMRemoteConfig;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

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

    public void guetClassTable(View view) {
        addGroup(getResources().getString(R.string.text_guetClassTable), getResources().getString(R.string.key_guetClassTable), 0);
    }

    public void guiLinLove(View view) {
        addGroup(getResources().getString(R.string.text_guilin_love), getResources().getString(R.string.key_guilin_love), 1);
    }

    public void addGroup(String name, String key, int num) {
        report(this, name);
        if (setting.charAt(num) == '1') {
            DialogUtil.showTextDialog(this, "该群因违反协议，已被禁止！");
        } else {
            AppUtil.joinQQGroup(key, this);
        }
    }

    public void report(Context context, String funcName) {
        Map<String, Object> funcMap = new HashMap<>();
        funcMap.put("name", funcName);
        MobclickAgent.onEventObject(context, "addGroup", funcMap);
    }

}
package top.yvyan.guettable.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.cconfig.UMRemoteConfig;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.table.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;

import static com.xuexiang.xui.XUI.getContext;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(this);
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_about);
        BackgroundUtil.setFullAlphaStatus(this);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_about));
        TextView profileVersion = findViewById(R.id.about_version);
        profileVersion.setText(AppUtil.getAppVersionName(getContext()));
    }

    public void doBack(View view) {
        finish();
    }

    public void addQQ(View view) {
        AppUtil.addQQ(this);
    }

    public void devSay(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.about_1) + "\r\n\r\n" + getResources().getString(R.string.about_2));
    }

    public void myGit(View view) {
        CommFunc.openBrowser(this, getResources().getString(R.string.github_url));
    }

    public void open(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.thanks_1) + "\r\n\r\n" + getResources().getString(R.string.thanks_2) + "\r\n\r\n和一些常用依赖，在此表示感谢！");
    }

    public void privacy(View view) {
        CommFunc.openUrl(this, "隐私政策", UMRemoteConfig.getInstance().getConfigValue("privacyUrl"), false);
    }

    public void join_us(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.join_us));
    }

    public void statement(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.first_about));
    }
}
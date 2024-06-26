package top.yvyan.guettable.moreFun;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;

public class MoreUrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(this);
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_more_url);

        ConstraintLayout header = findViewById(R.id.func_base_constraintLayout);
        header.getBackground().setAlpha(255);

        BackgroundUtil.setFullAlphaStatus(this);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.moreFun_url_more));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_url_more));
    }

    public void openBrowser(String url) {
        CommFunc.openUrl(this, null, url, true);
    }

    public void doBack(View view) {
        finish();
    }

    public void schoolSong(View view) {
        openBrowser(getResources().getString(R.string.url_school_song));
    }

    public void netLogin(View view) {
        openBrowser(getResources().getString(R.string.url_net_login));
    }

    public void CET(View view) {
        openBrowser(getResources().getString(R.string.url_cet));
    }

    public void mail(View view) {
        openBrowser(getResources().getString(R.string.url_mail));
    }

    public void training(View view) {
        CommFunc.checkVpn(this,
                getResources().getString(R.string.url_training),
                getResources().getString(R.string.url_training_vpn),
                "链接包含下载内容，将为您使用浏览器打开");
    }

    public void calendar(View view) {
        CommFunc.checkVpn(this,
                getResources().getString(R.string.url_calendar),
                getResources().getString(R.string.url_calendar_vpn),
                "链接包含下载内容，将为您使用浏览器打开");
    }

    public void libOpen(View view) {
        openBrowser(getResources().getString(R.string.url_lib_open));
    }

    public void map(View view) {
        openBrowser(getResources().getString(R.string.url_map));
    }

    public void staff(View view) {
        CommFunc.checkVpn(this,
                getResources().getString(R.string.url_staff),
                getResources().getString(R.string.url_staff_vpn),
                null);
    }
}
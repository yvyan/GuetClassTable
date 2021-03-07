package top.yvyan.guettable.moreFun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
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

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View addStatus = findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getApplicationContext()));
        addStatus.setLayoutParams(lp);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.moreFun_url_more));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_url_more));
    }

    public void openBrowser(String url) {
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        MobclickAgent.onEventObject(this, "openUrl", urlMap);
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    public void doBack(View view) {
        finish();
    }

    public void resourceCenter(View view) {
        openBrowser(getResources().getString(R.string.url_resource_center));
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
        openBrowser(getResources().getString(R.string.url_training));
    }

    public void calendar(View view) {
        openBrowser(getResources().getString(R.string.url_calendar));
    }

    public void libOpen(View view) {
        openBrowser(getResources().getString(R.string.url_lib_open));
    }
}
package top.yvyan.guettable.activity;

import static com.xuexiang.xui.XUI.getContext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.umcrash.UMCrash;

import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(this);
        SettingData Settings=SettingData.newInstance(getContext());
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_about);
        BackgroundUtil.setFullAlphaStatus(this);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_about));
        TextView profileVersion = findViewById(R.id.about_version);
        LinearLayout debug_ClearMFACookie= findViewById(R.id.clear_mfa_cookie);
        debug_ClearMFACookie.setVisibility(Settings.isDevelopMode() ? LinearLayout.VISIBLE : LinearLayout.GONE);
        View debug_ClearMFACookie_spliter= findViewById(R.id.clear_mfa_cookie_spliter);
        debug_ClearMFACookie_spliter.setVisibility(Settings.isDevelopMode() ? View.VISIBLE : View.GONE);
        profileVersion.setText(AppUtil.getAppVersionName(getContext()));
    }

    public void doBack(View view) {
        finish();
    }

    public void addQQ(View view) {
        AppUtil.addQQ(this);
    }

    public void devSay(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.about));
    }

    public void myGit(View view) {
        CommFunc.openBrowser(this, getResources().getString(R.string.gitee_url));
    }

    public void open(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.thanks_1) + "\r\n\r\n" + getResources().getString(R.string.thanks_2) + "\r\n\r\n和一些常用依赖，在此表示感谢！");
    }

    public void privacy(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.privacy_text));
    }

    public void join_us(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.join_us));
    }

    public void statement(View view) {
        DialogUtil.showTextDialog(this, getResources().getString(R.string.first_about));
    }

    public void helpTest(View view) {
        AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_help_test));
        helpTest();
    }

    public void clearMFACookie(View view) {
        TokenData tokenData = TokenData.newInstance(getContext());
        tokenData.setMFACookie(null);
        tokenData.setBkjwCookie(null);
        ToastUtil.showToast(getContext(),"清除成功");
    }

    /**
     * 协助测试
     */
    public void helpTest() {
        if (SettingData.newInstance(getContext()).isDevelopMode()) {
            Intent intent = new Intent(getContext(), HelpTestActivity.class);
            startActivity(intent);
        } else if (AppUtil.isWifi(Objects.requireNonNull(getContext()))) {
            DialogUtil.showTextDialog(this, "为了保证测试顺利，请关闭WIFI，连接数据网络后进行测试。");
        } else {
            ToastUtil.showToast(this, "请不要退出页面或者切换网络，正在获取凭证，最多需要30s，请稍后！");
            new Thread(() -> {
                TokenData tokenData = TokenData.newInstance(getContext());
                int n = tokenData.refresh();
                runOnUiThread(() -> {
                    try {
                        if (n == 0) {
                            //获取剪贴板管理器：
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", tokenData.getCookie());
                            // 将ClipData内容放到系统剪贴板里。
                            cm.setPrimaryClip(mClipData);
                            DialogUtil.showTextDialog(this, "感谢协助，凭证复制成功，您现在可以发送给开发者了！");
                        } else {
                            DialogUtil.showTextDialog(this, "获取失败，请稍后重试。");
                        }
                    } catch (Exception e) {
                        UMCrash.generateCustomLog(e, "helpTest");
                    }
                });
            }).start();
        }
    }
}
package top.yvyan.guettable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TextDialog;

import static com.xuexiang.xui.XUI.getContext;

public class AboutActivity extends AppCompatActivity {

    private TextView profileVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        profileVersion = findViewById(R.id.about_version);
        profileVersion.setText(AppUtil.getAppVersionName(getContext()));
    }

    public void doBack(View view) {
        finish();
    }

    public void addQQ(View view) {
        AppUtil.addQQ(this);
    }

    public void devSay(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.about_1) + "\r\n\r\n" + getResources().getString(R.string.about_2));
    }

    public void myGit(View view) {
        openBrowser(getResources().getString(R.string.github_url));
    }

    public void open(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.thanks_1) + "\r\n\r\n" + getResources().getString(R.string.thanks_2)+ "\r\n\r\n和一些常用依赖，在此表示感谢！");
    }

    public void privacy(View view) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_URL, getContext().getResources().getString(R.string.privacy_url));
        intent.putExtra(WebViewActivity.WEB_TITLE, "隐私政策");
        startActivity(intent);
    }

    public void openBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    public void join_us(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.join_us));
    }

    public void statement(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.first_about));
    }
}
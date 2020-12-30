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

    /****************
     *
     * 发起添加群流程。群号：桂电课程表交流群(963908505) 的 key 为： b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP
     * 调用 joinQQGroup(b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP) 即可发起手Q客户端申请加群 桂电课程表交流群(963908505)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    private boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public void doBack(View view) {
        finish();
    }

    public void addQQ(View view) {
        String key = "b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP";
        joinQQGroup(key);
    }

    public void devSay(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.about_1) + "\r\n\r\n" + getResources().getString(R.string.about_2));
    }

    public void myGit(View view) {
        openBrowser(getResources().getString(R.string.github_url));
    }

    public void open(View view) {
        TextDialog.showScanNumberDialog(this, getResources().getString(R.string.thanks_1) + "\r\n\r\n" + getResources().getString(R.string.thanks_2));
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
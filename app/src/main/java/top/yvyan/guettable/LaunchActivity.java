package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.app.FirstLoad;
import top.yvyan.guettable.util.TextDialog;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.app_white));              //设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //状态栏为白色 图标显示深色
        }

        FirstLoad firstLoad = new FirstLoad(getApplicationContext());
        firstLoad.check();

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= 19) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        int time = 150;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(() -> {
            GeneralData generalData = GeneralData.newInstance(getApplicationContext());
            if (!generalData.isApplyPrivacy()) {

                TextDialog.IDialogService service = new TextDialog.IDialogService() {
                    @Override
                    public int onClickYes() {
                        generalData.setApplyPrivacy(true);
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        LaunchActivity.this.finish();
                        return 0;
                    }

                    @Override
                    public int onClickBack() {
                        finish();
                        return 0;
                    }
                };
                TextDialog.showDialog(this, "隐私协议", false, "同意", "拒绝", getString(R.string.privacy_text), service);
            } else {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                LaunchActivity.this.finish();
            }
        }, time);
    }
}
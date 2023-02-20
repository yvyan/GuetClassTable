package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import top.yvyan.guettable.MainActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.baseFun.FirstLoad;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.util.DialogUtil;

public class LaunchActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.app_white));              //设置状态栏颜色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //状态栏为白色 图标显示深色

        FirstLoad firstLoad = new FirstLoad(getApplicationContext());
        firstLoad.check();

        window = this.getWindow();
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //网络切换检测
        initReceiver();

        int time = 30;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(() -> {
            GeneralData generalData = GeneralData.newInstance(getApplicationContext());
            if (!generalData.isApplyPrivacy()) {
                DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                    @Override
                    public void onClickYes() {
                        generalData.setApplyPrivacy(true);
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                        LaunchActivity.this.finish();
                    }

                    @Override
                    public void onClickBack() {
                        finish();
                    }
                };
                DialogUtil.showDialog(this, "隐私协议", false, "同意", "拒绝", getString(R.string.privacy_text), service);
            } else {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                LaunchActivity.this.finish();
            }
        }, time);
    }

    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission")
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    int type2 = networkInfo.getType();

                    switch (type2) {
                        case 0://移动 网络
                            TokenData.isVPN = true;
                            break;
                        case 1: //wifi网络
                            new Thread(() -> TokenData.isVPN = Net.testNet() == 200).start();
                            break;
                    }
                }
            }
        }
    };

    /**
     * 注册网络监听的广播
     */
    private void initReceiver() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED");
        timeFilter.addAction("android.net.ethernet.STATE_CHANGE");
        timeFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        timeFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        timeFilter.addAction("android.net.wifi.STATE_CHANGE");
        timeFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(netReceiver, timeFilter);
    }
}
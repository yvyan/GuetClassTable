package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.service.fetch.LAN;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AccountData accountData = AccountData.newInstance(this);

        new Thread(() -> {
            long time;

            time = System.currentTimeMillis();
            String VPNToken = LAN.getVPNToken(this);
            Log.d("1586", "time1:" + (System.currentTimeMillis() - time) + "ms; token:" + VPNToken);

            time = System.currentTimeMillis();
            int n = StaticService.loginVPN(this, VPNToken, accountData.getUsername(), accountData.getPassword());
            Log.d("1586", "time2:" + (System.currentTimeMillis() - time) + "ms; return:" + n);

            time = System.currentTimeMillis();
            n = StaticService.autoLoginV(this, accountData.getUsername(), accountData.getPassword(), VPNToken);
            Log.d("1586", "time3:" + (System.currentTimeMillis() - time) + "ms; return:" + n);

        }).start();
    }
}
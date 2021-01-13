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
            long time = System.currentTimeMillis();
//            LAN.getVPNToken(this);
//            Log.d("1586", "time1.1:" + (System.currentTimeMillis() - time) + "ms");

//            time = System.currentTimeMillis();
//            String VPNToken = LAN.getVPNToken1(this);
//            Log.d("1586", "time1.2:" + (System.currentTimeMillis() - time) + "ms");

            //time = System.currentTimeMillis();
            String TGTTokenStr = "TGT-3716-9UVF5663e5PKMLF2DJOBRpjd3j5-PRpeTk-wtNIypDh8Dl8VchsbNiS0rvx2RXeoDo8-30ed31dde02d";
            //Log.d("1586", "time2:" + (System.currentTimeMillis() - time) + "ms; return:" + TGTTokenStr);

            time = System.currentTimeMillis();
            String ST_VPN = StaticService.SSOGetST(this, TGTTokenStr, getResources().getString(R.string.service_vpn), true);
            Log.d("1586", "time3:" + (System.currentTimeMillis() - time) + "ms; return:" + ST_VPN);

            time = System.currentTimeMillis();
            int response = LAN.testNet(this);
            Log.d("1586", "time4:" + (System.currentTimeMillis() - time) + "ms; return:" + response);

        }).start();
    }

    public static String encrypt(String pwd) {
        int[] key = {134, 8, 187, 0, 251, 59, 238, 74, 176, 180, 24, 67, 227, 252, 205, 80};
        int pwd_len = pwd.length();
        try {
            if (pwd.length() % 16 != 0) {
                int need_num = 16 - pwd.length() % 16;
                StringBuilder pwd_builder = new StringBuilder();
                pwd_builder.append(pwd);
                for (int i = 0; i < need_num; i++) {
                    pwd_builder.append("0");
                }
                pwd = pwd_builder.toString();
            }
            byte[] pwd_bytes = pwd.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < pwd_bytes.length; i++) {
                pwd_bytes[i] ^= key[i % 16];
            }
            StringBuilder encrypt_builder = new StringBuilder();
            encrypt_builder.append("77726476706e6973617765736f6d6521");
            for (int i = 0; i < pwd_len; i++) {
                byte b = pwd_bytes[i];
                encrypt_builder.append(String.format("%02x", b));
            }
            return encrypt_builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import top.yvyan.guettable.Http.Get;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.Http.Post;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.service.fetch.LAN;
import top.yvyan.guettable.util.UrlReplaceUtil;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new Thread(() -> {
            String VPNToken = LAN.getVPNToken(this);
            String str = "TGT-686614-0NcBV5MSb4ZBvc0FPYJDIEgpAgTDYukndmtAKhxg3TPTHSafjXiLDfK9FOCz-srODyc-f67414573db3";
            Log.d("1586", str);
            String string = StaticService.SSOGetST(this, str, getResources().getString(R.string.service_vpn), true);
            String string2 = StaticService.SSOGetST(this, str, getResources().getString(R.string.service_bkjw), true);
            Log.d("1586", "vpn_st:" + string);
            Log.d("1586", "vpn_session:" + VPNToken);
            StaticService.loginVPN(string, VPNToken);


            int n = StaticService.loginBkjwVPN(string2, VPNToken);

            HttpConnectionAndCode httpConnectionAndCode = Get.get(
                    "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626012d46dbfe/Student/GetPerson",
                    null,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66",
                    "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626012d46dbfe/?ticket=ST-2098227-TeDMrO2dXhV-kcmrUd9sojVU9Rc-f67414573db3",
                    VPNToken,
                    null,
                    ";",
                    null,
                    null,
                    null,
                    null,
                    5000,
                    getResources().getString(R.string.SSO_context_type)
            );
            Log.d("1586", httpConnectionAndCode.comment);
        }).start();
    }

}
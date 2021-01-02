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
import top.yvyan.guettable.util.UrlReplaceUtil;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new Thread(() -> {
            //String str = StaticService.SSOLogin(this, "1800100607", "14001X", true);
            String str = "TGT-620190-DoWU8n7sxCiEQfLEqWq35hbXHJWASG5LykV-kxJt6WK4IepZQKbv--z1NZj-wZb2lqo-f67414573db3";
            Log.d("1586", str);
            String string = StaticService.SSOGetST(this, str, getResources().getString(R.string.service_vpn), true);
            String string2 = StaticService.SSOGetST(this, str, getResources().getString(R.string.service_bkjw), true);
            Log.d("1586", "vpn_st:" + string);
            StringBuilder builder = new StringBuilder();
            StaticService.loginVPN(this, string, builder);
            Log.d("1586", "vpn_session:" + builder.toString());
            //StringBuilder builder1 = new StringBuilder();
            //int n = StaticService.loginBkjw(this, string2, "wengine_vpn_ticket=9d55501c1007232c; refresh=1; show_vpn=1", builder1);

            //Log.d("1586", n + builder1.toString());
            Resources resources = getResources();
            HttpConnectionAndCode httpConnectionAndCode = Get.get(
                    "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626012d46dbfe/login/GetValidateCode",
                    null,
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66",
                    "http://172.16.13.22",
                     "wengine_vpn_ticket=3c923f07a70a5b53; refresh=1; show_vpn=1" +
                             "" +
                             "",
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
//            HttpConnectionAndCode login_res = Get.get(
//                    "https://v.guet.edu.cn",
//                    null,
//                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66",
//                    "https://v.guet.edu.cn/https/77726476706e69737468656265737421f3f652d220256d44300d8db9d6562d/cas/login?service=https%3A%2F%2Fv.guet.edu.cn%2Flogin%3Fcas_login%3Dtrue",
//                     "wengine_vpn_ticket=9d55501c1007232c",
//                    null,
//                    ";",
//                    null,
//                    null,
//                    null,
//                    null,
//                    5000,
//                    getResources().getString(R.string.SSO_context_type)
//            );
//            Log.d("1586", login_res.comment);
        }).start();
    }

}
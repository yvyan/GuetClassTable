package top.yvyan.guettable;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.data.UserData;
import top.yvyan.guettable.fetch.LAN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private EditText checkCodeInput;
    private CheckBox cbRememberPwd;
    private Button button;

    private UserData userData;

    private StringBuilder cookie_builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        userData = UserData.newInstance(this);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        checkCodeInput = findViewById(R.id.checkcode_input);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        cbRememberPwd.setChecked(true);
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bPwdSwitch = !bPwdSwitch;
                if (bPwdSwitch) {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });

        if (userData.getIsSave()) {
            etAccount.setText(userData.getUsername());
            etPwd.setText(userData.getPassword());
        }

        changeCode();
    }

    @Override
    public void onClick(View view) {
        toLogin();
        button.setBackgroundColor(0x44444444);
        button.setText("正在登陆");
        button.setEnabled(false);
    }

    public void toLogin() {
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        String checkCode = checkCodeInput.getText().toString();

        final String cookie_before_login = cookie_builder.toString();
        new Thread(() -> {
            HttpConnectionAndCode login_res = LAN.login(this, account, pwd, checkCode, cookie_before_login, cookie_builder);
            if (login_res.code != 0) { //登陆失败
                String msg;
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    msg = getResources().getString(R.string.lan_login_fail_ck);
                } else if (login_res.comment != null && login_res.comment.contains("密码")) {
                    msg = getResources().getString(R.string.lan_login_fail_pwd);
                } else {
                    msg = getResources().getString(R.string.lan_login_fail);
                }
                Looper.prepare();
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                runOnUiThread(() -> {
                    button.setText("登录");
                    button.setBackgroundColor(0xFF03A9F4);
                    button.setEnabled(true);
                });
                Looper.loop();
            }
            //登陆成功
            runOnUiThread(() -> {
                button.setText("登陆成功");
            });
        }).start();
    }

//    @Override
//    public void onClick(View view) {
//        userData.setUser(etAccount.getText().toString(), etPwd.getText().toString(), cbRememberPwd.isChecked());
//        getCourseTable();
//
//        button.setBackgroundColor(0x44444444);
//        button.setText("正在登陆");
//        button.setEnabled(false);
//        //finish();
//    }

    /**
     * 刷新验证码
     */
    public void changeCode() {
        final ImageView imageView = findViewById(R.id.imageView_checkcode);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.network, getTheme()));
        checkCodeInput.setText("");

        cookie_builder = new StringBuilder();
        new Thread(() -> {
            final HttpConnectionAndCode res = LAN.checkCode(this);
            if (res.obj != null) {
                //TODO "telephone"标识符含义待确认
                final String ocr = OCR.getTextFromBitmap(this, (Bitmap) res.obj, "telephone");
                cookie_builder.append(res.cookie);

                runOnUiThread(() -> {
                    imageView.setImageBitmap((Bitmap)res.obj);
                    checkCodeInput.setText(ocr);
                });
            }
        }).start();
    }

    private void getCourseTable() {
        final OkHttpClient client;
        client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
                cookieStore.put(HttpUrl.parse(getString(R.string.url_login)), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(HttpUrl.parse(getString(R.string.url_login)));
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", userData.getUsername())
                .add("passwd", userData.getPassword())
                .add("login","%B5%C7%A1%A1%C2%BC")
                .add("mCode","000703")
                .build();//创建网络请求表单

        final Request request = new Request.Builder()
                .url(this.getString(R.string.url_login))
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "网络请求错误！", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //存储用户名密码

                //获取课程表
                RequestBody requestBody1 = new FormBody.Builder()
                        .add("term", "2020-2021_1")
                        .build();
                Request request1 = new Request.Builder()
                        .url(getString(R.string.url_course))
                        .addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
                        .post(requestBody1)
                        .build();
                call = client.newCall(request1);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "网络请求错误！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] data = response.body().bytes();

                        final String string = new String(data, "gb2312");
                        Log.d("CourseData:", string);
                        userData.setCourse(string);
                        response.body().close();
                        finish();
                    }
                });
            }
        });

    }
}
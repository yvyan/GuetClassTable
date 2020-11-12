package top.yvyan.guettable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.service.fetch.LAN;
import top.yvyan.guettable.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private EditText checkCodeInput;
    private CheckBox cbRememberPwd;
    private Button button;

    private AccountData accountData;
    private StringBuilder cookie_builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        accountData = AccountData.newInstance(this);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        checkCodeInput = findViewById(R.id.checkcode_input);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);

        cbRememberPwd.setChecked(true);
        ivPwdSwitch.setOnClickListener((View view) -> {
            bPwdSwitch = !bPwdSwitch;
            if (bPwdSwitch) {
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd.setTypeface(Typeface.DEFAULT);
            }
        });

        if (accountData.getIsSave()) {
            etAccount.setText(accountData.getUsername());
            etPwd.setText(accountData.getPassword());
        }

        changeCode();
    }

    @Override
    public void onClick(View view) {
        button.setBackgroundColor(0x44444444);
        button.setText("正在登录");
        button.setEnabled(false);
        testAccount(
                etAccount.getText().toString(),
                etPwd.getText().toString(),
                checkCodeInput.getText().toString()
        );
    }

    /**
     * 尝试登陆
     * 若密码正确，则存储密码
     * @param account   学号
     * @param pwd       密码
     * @param checkCode 验证码
     */
    private void testAccount(String account, String pwd, String checkCode) {
        final String cookie_before_login = cookie_builder.toString();
        if ("".equals(account) || "".equals(pwd)) {
            runOnUiThread(() -> {
                ToastUtil.showToast(this, "请输入学号/密码");
                button.setText("登录");
                button.setBackgroundColor(0xFF03A9F4);
                button.setEnabled(true);
            });
            return;
        }
        new Thread(() -> {
            HttpConnectionAndCode login_res = LAN.login(this, account, pwd, checkCode, cookie_before_login, cookie_builder);
            if (login_res.code != 0) { //登录失败
                String msg;
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    msg = getResources().getString(R.string.lan_login_fail_ck);
                } else if (login_res.comment != null && login_res.comment.contains("密码")) {
                    msg = getResources().getString(R.string.lan_login_fail_pwd);
                } else {
                    //TODO: 修改为ToastUtil
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
            } else { //登录成功
                accountData.setUser(account, pwd, cbRememberPwd.isChecked());

                //启动SetTerm活动
                Intent intent = new Intent(this, SetTermActivity.class);
                intent.putExtra("cookie", cookie_builder.toString());
                startActivity(intent);
                finish();
            }
        }).start();
    }

    /**
     * 刷新验证码
     */
    public void changeCode() {
        final ImageView imageView = findViewById(R.id.imageView_checkcode);
        imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.network, getTheme()));
        checkCodeInput.setText("");

        cookie_builder = new StringBuilder();
        new Thread(() -> {
            final HttpConnectionAndCode res = LAN.checkCode(this);
            if (res.obj != null) {
                final String ocr = OCR.getTextFromBitmap(this, (Bitmap) res.obj, "telephone");
                cookie_builder.append(res.cookie);

                runOnUiThread(() -> {
                    imageView.setImageBitmap((Bitmap)res.obj);
                    checkCodeInput.setText(ocr);
                });
            }
        }).start();
    }

}
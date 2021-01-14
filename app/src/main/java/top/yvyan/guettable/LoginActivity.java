package top.yvyan.guettable;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private SuperButton button;

    private AccountData accountData;
    private int type; //登录方式选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        type = TokenData.newInstance(this).getLoginType();

        accountData = AccountData.newInstance(this);
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
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
    }

    @Override
    public void onClick(View view) {
        setUnClick();
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();

        new Thread(() -> {
            int state;
            if (type == 0) { //CAS登录
                state = testLoginCAS(account, pwd);
                if (state == -2) {
                    TokenData.isVPN = true;
                    state = testLoginCAS(account, pwd);
                }
            } else if (type == 1){ //VPN + 教务登录
                state = testLoginBkjw(account, pwd);
            } else {
                state = -2;
            }
            if (state == 0) {
                TokenData tokenData = TokenData.newInstance(this);
                tokenData.setLoginType(type);
                accountData.setUser(account, pwd, cbRememberPwd.isChecked());
                tokenData.refresh();
                Intent intent = new Intent(this, SetTermActivity.class);
                intent.putExtra("fromLogin", ""); //便于识别启动类
                startActivity(intent);
                finish();
            } else {
                int finalState = state;
                runOnUiThread(() -> {
                    switch (finalState) {
                        case -1:
                            ToastUtil.showToast(this, getResources().getString(R.string.lan_login_fail_pwd));
                            break;
                        case -2:
                            ToastUtil.showToast(this, getResources().getString(R.string.lan_login_fail));
                            break;
                        case -3:
                            ToastUtil.showToast(this, getResources().getString(R.string.lan_login_fail_ck));
                            break;
                    }
                    setEnClick();
                });
            }
        }).start();
    }

    /**
     * 测试CAS登录
     *
     * @param account  学号
     * @param password 密码
     * @return 操作结果
     */
    public int testLoginCAS(String account, String password) {
        String TGTTokenStr = StaticService.SSOLogin(this, account, password, TokenData.isVPN);
        if (TGTTokenStr.equals("ERROR2")) {
            return -2;
        }
        if (TGTTokenStr.contains("TGT-")) {
            TokenData.newInstance(this).setTGTToken(TGTTokenStr);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 测试教务登录
     *
     * @param account  学号
     * @param password 密码
     * @return 操作结果
     *          0 -- 登录成功
     *         -1 -- 密码错误
     *         -2 -- 网络错误/未知错误
     *         -3 -- 验证码连续错误
     */
    public int testLoginBkjw(String account, String password) {
        StringBuilder cookie_builder = new StringBuilder();
        int state = StaticService.autoLogin(
                this,
                accountData.getUsername(),
                accountData.getPassword(),
                cookie_builder
        );
        if (state == 0) {
            TokenData.newInstance(this).setBkjwCookie(cookie_builder.toString());
        }
        return state;
    }

    /**
     * 设置按钮可点击
     */
    private void setEnClick() {
        button.setText("登录");
        button.setEnabled(true);
    }

    /**
     * 设置按钮不可点击
     */
    private void setUnClick() {
        button.setText("正在登录");
        button.setEnabled(false);
    }

    public void chooseFun1(View view) {
        //修改图标颜色
        //修改相应的布局
        //设置登录类型
        type = 0;
    }

    public void chooseFun2(View view) {
        //修改图标颜色
        //修改相应的布局
        //设置登录类型
        type = 1;
    }
}
package top.yvyan.guettable;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private SuperButton button;

    private AccountData accountData;

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
        StringBuilder cookieBuilder = new StringBuilder();
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        new Thread(() -> {
            GeneralData.newInstance(this).setInternational(false);
            int state = StaticService.autoLogin(
                    this,
                    account,
                    pwd,
                    cookieBuilder
            );
            if (state == -1) {
                GeneralData.newInstance(this).setInternational(true);
                state = StaticService.autoLogin(
                        this,
                        account,
                        pwd,
                        cookieBuilder
                );
                if (state == -1) {
                    GeneralData.newInstance(this).setInternational(false);
                }
            }
            if (state == 0) {
                accountData.setUser(account, pwd, cbRememberPwd.isChecked());
                TokenData.newInstance(this).refresh();
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
}
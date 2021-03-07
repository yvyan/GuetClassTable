package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import org.jetbrains.annotations.NotNull;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.service.table.fetch.Net;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Boolean bPwdSwitch = false;
    private Boolean bPwdSwitch2 = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private SuperButton button;
    private RelativeLayout passwordSecondView;
    private EditText etPwd2;
    private View passwordHelp;
    private ImageView ivPwdSwitch;
    private ImageView ivPwdSwitch2;
    private View progressBar;

    private AccountData accountData;
    private GeneralData generalData;
    private int type; //登录方式选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type = TokenData.newInstance(this).getLoginType();

        accountData = AccountData.newInstance(getContext());
        generalData = GeneralData.newInstance(getContext());

        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        ivPwdSwitch2 = findViewById(R.id.iv_pwd_switch_2);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        passwordSecondView = findViewById(R.id.password_second);
        passwordSecondView.setVisibility(View.GONE);
        etPwd2 = findViewById(R.id.et_pwd_VPN);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        cbRememberPwd.setChecked(true);
        passwordHelp = findViewById(R.id.password_help);
        ivPwdSwitch.setOnClickListener(showPwdClickListener());
        ivPwdSwitch2.setOnClickListener(showPwdClickListener());
        progressBar = findViewById(R.id.progressBar2);

        if (accountData.getIsSave()) {
            etAccount.setText(accountData.getUsername());
            etPwd.setText(accountData.getPassword());
            etPwd2.setText(accountData.getPassword2());
        }
        TabControlView tabControlView = findViewById(R.id.TabControl);
        try {
            tabControlView.setDefaultSelection(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == 1) {
            passwordSecondView.setVisibility(View.VISIBLE);
            etPwd.setHint("请输入教务密码");
            passwordHelp.setVisibility(View.GONE);
        }
        tabControlView.setOnTabSelectionChangedListener((title, value) -> {
            if ("教务登录".equals(title)) { // 切换第二个密码框的显示
                passwordSecondView.setVisibility(View.VISIBLE);
                etPwd.setHint("请输入教务密码");
                passwordHelp.setVisibility(View.GONE);
                type = 1;
            } else {
                passwordSecondView.setVisibility(View.GONE);
                etPwd.setHint("请输入智慧校园密码");
                passwordHelp.setVisibility(View.VISIBLE);
                type = 0;
            }
        });
    }

    @NotNull
    private View.OnClickListener showPwdClickListener() {
        return (View view) -> {
            bPwdSwitch = !bPwdSwitch;
            bPwdSwitch2 = !bPwdSwitch2;
            if (bPwdSwitch) {
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                ivPwdSwitch2.setImageResource(R.drawable.ic_baseline_visibility_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                ivPwdSwitch2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd.setTypeface(Typeface.DEFAULT);
                etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd2.setTypeface(Typeface.DEFAULT);
            }
        };
    }

    @Override
    public void onClick(View view) {
        setUnClick();
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        new Thread(() -> {
            int state;
            generalData.setInternational(isInternational(account));
            if (type == 0) { //CAS登录
                if (generalData.isInternational()) {
                    runOnUiThread(() -> {
                        DialogUtil.showTextDialog(this, "国际学院同学目前只支持教务登录，请点击上方切换教务登录重试！");
                        setEnClick();
                    });
                    return;
                }
                state = testLoginCAS(account, pwd);
                if (state == -2) {
                    TokenData.isVPN = true;
                    state = testLoginCAS(account, pwd);
                }
            } else if (type == 1) { //VPN + 教务登录
                state = testLoginBkjw(account, pwd, pwd2);
            } else {
                state = -2;
            }
            if (state == 0) {
                runOnUiThread(() -> button.setText("正在登录"));
                TokenData tokenData = TokenData.newInstance(this);
                tokenData.setLoginType(type);
                accountData.setUser(account, pwd, cbRememberPwd.isChecked());
                if (type == 1) {
                    if (pwd2.isEmpty()) {
                        accountData.setPassword2(pwd);
                    } else {
                        accountData.setPassword2(pwd2);
                    }
                }
                tokenData.refresh();
                runOnUiThread(() -> button.setText("获取个人信息"));
                StudentInfo studentInfo = StaticService.getStudentInfo(this, TokenData.newInstance(this).getCookie());
                GeneralData generalData = GeneralData.newInstance(this);
                if (studentInfo != null) {
                    generalData.setNumber(studentInfo.getStid());
                    generalData.setName(studentInfo.getName());
                    generalData.setTerm(studentInfo.getTerm());
                    generalData.setGrade(studentInfo.getGrade());
                    Intent intent = new Intent(this, SetTermActivity.class);
                    startActivity(intent);
                    finish();
                } else { //若获取个人信息失败，则登出，否则会导致后续获取不到年级等信息导致闪退
                    runOnUiThread(() -> {
                        ToastUtil.showToast(this, getResources().getString(R.string.login_fail_getInfo));
                        accountData.logoff();
                        setEnClick();
                    });
                }
            } else {
                int finalState = state;
                runOnUiThread(() -> {
                    switch (finalState) {
                        case -1:
                            if (type == 0) {
                                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_pwd));
                            } else {
                                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_bkjw));
                            }
                            break;
                        case -2:
                            ToastUtil.showToast(this, getResources().getString(R.string.login_fail));
                            break;
                        case -3:
                            ToastUtil.showToast(this, getResources().getString(R.string.login_fail_ck));
                            break;
                        case -4:
                            ToastUtil.showToast(this, getResources().getString(R.string.login_fail_vpn));
                    }
                    setEnClick();
                });
            }
        }).start();
    }

    /**
     * 确定是否为国际学院账号
     *
     * @param account 学号
     * @return 是否为国际学院账号
     */
    private boolean isInternational(String account) {
        if (account.isEmpty()) {
            return false;
        } else return account.length() == 10 && account.startsWith("611", 2);
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
        runOnUiThread(() -> button.setText("正在认证"));
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
     * @param passwordVPN VPN密码
     * @return 操作结果
     *  0 -- 登录成功
     * -1 -- 教务密码错误
     * -2 -- 网络错误/未知错误
     * -3 -- 验证码连续错误
     * -4 -- VPN密码错误
     */
    @SuppressLint("SetTextI18n")
    public int testLoginBkjw(String account, String password, String passwordVPN) {
        TokenData tokenData = TokenData.newInstance(this);
        String VPNToken = Net.getVPNToken(this);
        tokenData.setVPNToken(VPNToken);
        if (passwordVPN.isEmpty()) {
            passwordVPN = password;
        }
        runOnUiThread(() -> button.setText("验证VPN"));
        int n = StaticService.loginVPN(this, VPNToken, account, passwordVPN);
        if (n != 0) {
            n = -4;
        }
        if (TokenData.isVPN) {
            if (n == 0) {
                runOnUiThread(() -> button.setText("验证教务"));
                n = StaticService.autoLoginV(this, account, password, VPNToken);
            }
        } else {
            if (n == 0) {
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
        }
        return n;
    }

    /**
     * 设置按钮可点击
     */
    private void setEnClick() {
        button.setText("登录");
        button.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 设置按钮不可点击
     */
    private void setUnClick() {
        button.setText("网络初始化");
        button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 打开智慧校园网址
     *
     * @param view view
     */
    public void firstLogin(View view) {
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(getContext().getResources().getString(R.string.smart_campus));
        webIntent.setData(uri);
        startActivity(webIntent);
    }
}
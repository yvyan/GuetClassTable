package top.yvyan.guettable.activity;

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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.umeng.umcrash.UMCrash;
import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

public class LoginActivity extends Activity implements View.OnClickListener {
    public static int REQUEST_CODE = 13;
    public static int OK = 10;

    private Boolean bPwdSwitch = false;
    private Boolean bPwdSwitch2 = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private SuperButton button;
    private RelativeLayout bkjwPasswordView;
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
        bkjwPasswordView = findViewById(R.id.bkjwPassword);
        etPwd2 = findViewById(R.id.et_pwd2);
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        cbRememberPwd.setChecked(true);
        passwordHelp = findViewById(R.id.password_help);
        ivPwdSwitch.setOnClickListener(showPwdClickListener());
        ivPwdSwitch2.setOnClickListener(showPwdClickListener());
        progressBar = findViewById(R.id.progressBar2);
        TextView profileVersion = findViewById(R.id.tv_profile_version);
        profileVersion.setText(AppUtil.getAppVersionName(Objects.requireNonNull(getContext())));
        //获取账号密码
        if (accountData.getIsSave()) {
            etAccount.setText(accountData.getUsername());
            etPwd.setText(accountData.getBkjwPwd());
            etPwd2.setText(accountData.getVPNPwd());
        }
        //选择登录方式
        TabControlView tabControlView = findViewById(R.id.TabControl);
        try {
            tabControlView.setDefaultSelection(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //智慧校园登录方式隐藏教务密码输入框、显示登录提示
        if (type == 0) {
            bkjwPasswordView.setVisibility(View.GONE);
            passwordHelp.setVisibility(View.VISIBLE);
        }

        tabControlView.setOnTabSelectionChangedListener((title, value) -> {
            if ("教务登录".equals(title)) { // 切换第二个密码框的显示
                bkjwPasswordView.setVisibility(View.VISIBLE);
                passwordHelp.setVisibility(View.GONE);
                type = 1;
            } else {
                bkjwPasswordView.setVisibility(View.GONE);
                passwordHelp.setVisibility(View.VISIBLE);
                type = 0;
            }
        });
    }

    /**
     * 显示密码图标点击回调
     *
     * @return View.OnClickListener
     */
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

    /**
     * 登录按钮点击回调
     *
     * @param view view
     */
    @Override
    public void onClick(View view) {
        setUnClick();
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        new Thread(() -> {
            //区分是否为国际学院
            generalData.setInternational(isInternational(account));
            //区分登录方式
            String VPNToken = Net.getVPNToken(this);
            if (type == 0) { //智慧校园
                int n = StaticService.loginVPN(this, VPNToken, account, pwd2);
                if (n != 0) { //VPN异常
                    if (n == -3) { //修改密码
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //修改密码
                                setEnClick();
                                changePwd(view);
                            }

                            @Override
                            public void onClickBack() {
                                testCAS(account, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "修改密码", "仍然继续", getContext().getResources().getString(R.string.log_vpn_pwd_easy), service));

                    } else if (n == -1) { //密码错误
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //重试
                                setEnClick();
                            }

                            @Override
                            public void onClickBack() {
                                testCAS(account, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "重试", "仍然继续", getContext().getResources().getString(R.string.log_vpn_pwd_error), service));

                    } else { //网络或VPN系统异常
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //重试
                                setEnClick();
                            }

                            @Override
                            public void onClickBack() {
                                testCAS(account, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "重试", "仍然继续", getContext().getResources().getString(R.string.log_vpn_net_error), service));
                    }
                } else { //正常登录VPN，因为智慧校园和VPN密码相同，则不进行验证和登录，在getInfo()方法刷新Token时进行登录
                    TokenData tokenData = TokenData.newInstance(this);
                    tokenData.setLoginType(0);
                    tokenData.setTGTToken("TGT-");
                    accountData.setUser(account, null, pwd2, cbRememberPwd.isChecked());
                    getInfo();
                }
            } else if (type == 1) {
                int n = StaticService.loginVPN(this, VPNToken, account, pwd2);
                if (n != 0) { //VPN异常
                    if (n == -3) { //修改密码
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //修改密码
                                setEnClick();
                                changePwd(view);
                            }

                            @Override
                            public void onClickBack() {
                                testBKJW(account, pwd, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "修改密码", "仍然继续", getContext().getResources().getString(R.string.log_vpn_pwd_easy), service));

                    } else if (n == -1) { //密码错误
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //重试
                                setEnClick();
                            }

                            @Override
                            public void onClickBack() {
                                testBKJW(account, pwd, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "重试", "仍然继续", getContext().getResources().getString(R.string.log_vpn_pwd_error), service));

                    } else { //网络或VPN系统异常
                        DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                            @Override
                            public void onClickYes() {
                                //重试
                                setEnClick();
                            }

                            @Override
                            public void onClickBack() {
                                testBKJW(account, pwd, pwd2);
                            }
                        };
                        runOnUiThread(() -> DialogUtil.showDialog(this, "VPN异常", false, "重试", "仍然继续", getContext().getResources().getString(R.string.log_vpn_net_error), service));
                    }

                } else { //正常登录VPN
                    testBKJW_VPN(account, pwd, pwd2, VPNToken);
                }
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SetTermActivity.REQUEST_CODE && resultCode == SetTermActivity.OK) {
            Intent intent = getIntent();
            setResult(OK, intent);
            finish();
        }
    }

    /**
     * 验证教务密码（内网）
     *
     * @param account 学号
     * @param pwdBKJW 教务密码
     * @param pwdVPN  智慧校园/VPN密码
     */
    private void testBKJW(String account, String pwdBKJW, String pwdVPN) {
        new Thread(() -> {
            runOnUiThread(() -> button.setText("验证教务"));
            StringBuilder cookie_builder = new StringBuilder();
            int state = StaticService.autoLogin(
                    this,
                    account,
                    pwdBKJW,
                    cookie_builder
            );
            if (state == 0) {
                TokenData tokenData = TokenData.newInstance(this);
                tokenData.setBkjwCookie(cookie_builder.toString());
                tokenData.setLoginType(1);
                accountData.setUser(account, pwdBKJW, pwdVPN, cbRememberPwd.isChecked());
                getInfo();
            } else {
                showErrorToast(state);
            }
        }).start();
    }

    /**
     * 验证教务密码（VPN）
     *
     * @param account  学号
     * @param pwdBKJW  教务密码
     * @param pwdVPN   智慧校园/VPN密码
     * @param VPNToken VPNToken
     */
    private void testBKJW_VPN(String account, String pwdBKJW, String pwdVPN, String VPNToken) {
        new Thread(() -> {
            int state;
            TokenData tokenData = TokenData.newInstance(this);
            runOnUiThread(() -> button.setText("验证教务"));
            if (TokenData.isVPN) {
                state = StaticService.autoLoginV(this, account, pwdBKJW, VPNToken);
                if (state == 0) {
                    tokenData.setVPNToken(VPNToken);
                }
            } else {
                StringBuilder cookie_builder = new StringBuilder();
                state = StaticService.autoLogin(
                        this,
                        account,
                        pwdBKJW,
                        cookie_builder
                );
                if (state == 0) {
                    tokenData.setBkjwCookie(cookie_builder.toString());
                }
            }
            if (state == 0) {
                tokenData.setLoginType(1);
                accountData.setUser(account, pwdBKJW, pwdVPN, cbRememberPwd.isChecked());
                getInfo();
            } else {
                showErrorToast(state);
            }
        }).start();
    }

    /**
     * 验证智慧校园密码
     *
     * @param account  学号
     * @param password 智慧校园/VPN密码
     */
    private void testCAS(String account, String password) {
        new Thread(() -> {
            runOnUiThread(() -> button.setText("正在认证"));
            String TGTTokenStr = StaticService.SSOLogin(this, account, password, null);
            if (TGTTokenStr.contains("TGT-")) {
                TokenData tokenData = TokenData.newInstance(this);
                tokenData.setTGTToken(TGTTokenStr);
                tokenData.setLoginType(0);
                accountData.setUser(account, null, password, cbRememberPwd.isChecked());
                getInfo();
            } else {
                if (TGTTokenStr.equals("ERROR1")) {
                    showErrorToast(-4);
                } else if (TGTTokenStr.equals("ERROR2")) {
                    showErrorToast(-2);
                } else {
                    showErrorToast(-8);
                }
            }
        }).start();
    }

    /**
     * 显示登录错误信息
     *
     * @param n -1 : 教务密码错误
     *          -2 : 使用校园网
     *          -3 : 验证码错误
     *          -4 : 密码错误
     *          else : 未知错误
     */
    private void showErrorToast(int n) {
        runOnUiThread(() -> {
            setEnClick();
            if (n == -1) {
                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_bkjw));
            } else if (n == -2) {
                ToastUtil.showToast(this, getResources().getString(R.string.login_net_bkjw));
            } else if (n == -3) {
                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_ck));
            } else if (n == -4) {
                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_pwd));
            } else {
                ToastUtil.showToast(this, getResources().getString(R.string.login_fail));
            }
        });
    }

    /**
     * 获取个人信息
     */
    private void getInfo() {
        runOnUiThread(() -> button.setText("获取个人信息"));
        TokenData.newInstance(this).refresh();
        StudentInfo studentInfo = null;
        try {
            studentInfo = StaticService.getStudentInfo(this, TokenData.newInstance(this).getCookie());
        } catch (Exception e) {
            UMCrash.generateCustomLog(e, "getInfo");
        }
        GeneralData generalData = GeneralData.newInstance(this);
        if (studentInfo != null) {
            generalData.setNumber(studentInfo.getStid());
            generalData.setName(studentInfo.getName());
            generalData.setTerm(studentInfo.getTerm());
            generalData.setGrade(studentInfo.getGrade());
            Intent intent = new Intent(this, SetTermActivity.class);
            startActivityForResult(intent, SetTermActivity.REQUEST_CODE);
            runOnUiThread(this::setEnClick);
        } else { //若获取个人信息失败，则登出，否则会导致后续获取不到年级等信息导致闪退
            runOnUiThread(() -> {
                ToastUtil.showToast(this, getResources().getString(R.string.login_fail_getInfo));
                accountData.logoff();
                setEnClick();
            });
        }
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
     * 设置按钮可点击
     */
    private void setEnClick() {
        button.setText("登录");
        button.setEnabled(true);
        etAccount.setEnabled(true);
        etPwd.setEnabled(true);
        etPwd2.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 设置按钮不可点击
     */
    private void setUnClick() {
        button.setText("网络初始化");
        button.setEnabled(false);
        etAccount.setEnabled(false);
        etPwd.setEnabled(false);
        etPwd2.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void openUrl(String url) {
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    /**
     * 打开智慧校园网址
     *
     * @param view view
     */
    public void firstLogin(View view) {
        openUrl(getContext().getResources().getString(R.string.smart_campus));
    }

    /**
     * 打开修改智慧校园/vpn密码网址
     *
     * @param view view
     */
    public void changePwd(View view) {
        DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
            @Override
            public void onClickYes() {
                openUrl(getContext().getResources().getString(R.string.url_change_vpn_pwd));
            }

            @Override
            public void onClickBack() {
            }
        };
        DialogUtil.showProgress(this, getContext().getResources().getString(R.string.log_change_pwd), "好的", iDialogService);
    }
}
package top.yvyan.guettable.activity;

import static com.xuexiang.xui.XUI.getContext;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.umeng.umcrash.UMCrash;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class LoginActivity extends Activity implements View.OnClickListener {
    public static int REQUEST_CODE = 13;
    public static int OK = 10;

    private Boolean bPwdSwitch = false;
    private Boolean bPwdSwitch2 = false;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private SuperButton button;
    private EditText etPwd2;
    private ImageView ivPwdSwitch2;
    private View progressBar;

    private AccountData accountData;
    private GeneralData generalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountData = AccountData.newInstance(getContext());
        generalData = GeneralData.newInstance(getContext());

        ivPwdSwitch2 = findViewById(R.id.iv_pwd_switch_2);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        etPwd2 = findViewById(R.id.et_pwd2);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        cbRememberPwd.setChecked(true);
        ivPwdSwitch2.setOnClickListener(showPwdClickListener());
        progressBar = findViewById(R.id.progressBar2);
        TextView profileVersion = findViewById(R.id.tv_profile_version);
        profileVersion.setText(AppUtil.getAppVersionName(Objects.requireNonNull(getContext())));
        //获取账号密码
        if (accountData.getIsSave()) {
            etAccount.setText(accountData.getUsername());
            etPwd2.setText(accountData.getVPNPwd());
        }
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
                ivPwdSwitch2.setImageResource(R.drawable.ic_baseline_visibility_24);
                etPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch2.setImageResource(R.drawable.ic_baseline_visibility_off_24);
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
        String pwd2 = etPwd2.getText().toString();
        new Thread(() -> {
            String VPNToken = null;
            if (Net.testNet() != 200) {
                VPNToken = Net.getVPNToken(this);
            }
            testCAS(account, pwd2, VPNToken);
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
     * 验证智慧校园密码 OTP Version
     *
     */
    private void testCASWithOTP(String OTP,String CASCookie, String VPNToken,TokenData tokenData) {
        String account = etAccount.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        new Thread(() -> {
            runOnUiThread(() -> button.setText("正在认证-手机验证码"));
            String MulitFactorAuth = StaticService.VerifyPhoneOTP(this, CASCookie,OTP, VPNToken);
            if(MulitFactorAuth.contains("ERROR")) {
                if (MulitFactorAuth.equals("ERROR1")) {
                    showErrorToast(-4);
                } else if (MulitFactorAuth.equals("ERROR2")) {
                    showErrorToast(-2);
                } else {
                    showErrorToast(-8);
                }
            } else {
                tokenData.setCASCookie(CASCookie+"; "+MulitFactorAuth);
                tokenData.setBkjwCookie(null);
                accountData.setUser(account, null, pwd2, cbRememberPwd.isChecked());
                getInfo();
            }
        }).start();
    }

    /**
     * 验证智慧校园密码
     *
     * @param account  学号
     * @param password 智慧校园/VPN密码
     */
    private void testCAS(String account, String password, String VPNToken) {
        new Thread(() -> {
            runOnUiThread(() -> button.setText("正在认证"));
            String CasCookie = StaticService.SSOLogin(this, account, password, VPNToken);
            if (CasCookie.contains("TGT-")) {
                TokenData tokenData = TokenData.newInstance(this);
                if(CasCookie.contains("ERROR5")) {
                    tokenData.setCASCookie(CasCookie.substring(CasCookie.indexOf(";")+1));
                    tokenData.setBkjwCookie(null);
                    fuck2FA(account,password,CasCookie.substring(CasCookie.indexOf(";")+1),VPNToken,tokenData);
                    if(false) {
                        String phoneNumber = StaticService.SendPhoneOTP(this, CasCookie.substring(CasCookie.indexOf(";") + 1), account, VPNToken);
                        if (!phoneNumber.contains("ERROR")) {
                            runOnUiThread(() -> {
                                showPhoneOtpDialog(phoneNumber, CasCookie.substring(CasCookie.indexOf(";") + 1), VPNToken, tokenData);
                            });
                        } else {
                            if (phoneNumber.equals("ERROR1")) {
                                showErrorToast(-4);
                            } else if (phoneNumber.equals("ERROR2")) {
                                showErrorToast(-2);
                            } else if (phoneNumber.contains("ERROR3")) {
                                runOnUiThread(() -> {
                                    setEnClick();
                                    ToastUtil.showToast(this, getResources().getString(R.string.login_fail_phoneOTPSend) + phoneNumber.substring(7));
                                });
                            } else {
                                showErrorToast(-8);
                            }
                        }
                    }
                } else {
                    tokenData.setCASCookie(CasCookie);
                    tokenData.setBkjwCookie(null);
                    accountData.setUser(account, null, password, cbRememberPwd.isChecked());
                    getInfo();
                }
            } else {
                if (CasCookie.equals("ERROR1")) {
                    showErrorToast(-4);
                } else if (CasCookie.equals("ERROR2")) {
                    showErrorToast(-2);
                } else {
                    showErrorToast(-8);
                }
            }
        }).start();
    }


    private void fuck2FA(String account,String password,String CASCookie,String VPNToken,TokenData tokenData) {
        try{
                runOnUiThread(() -> button.setText("正在尝试绕过二步验证"));
                String MulitFactorAuth = StaticService.fuck2FA(this,password,CASCookie,VPNToken);
                if(MulitFactorAuth.contains("ERROR")) {
                    if (MulitFactorAuth.equals("ERROR1")) {
                        showErrorToast(-4);
                    } else if (MulitFactorAuth.equals("ERROR2")) {
                        showErrorToast(-2);
                    } else {
                        showErrorToast(-8);
                    }
                } else {
                    tokenData.setCASCookie(CASCookie + "; " + MulitFactorAuth);
                    tokenData.setBkjwCookie(null);
                    accountData.setUser(account, null, password, cbRememberPwd.isChecked());
                    getInfo();
                }

        } catch (Exception ignore) {
            return;
        }
    }
    /**
     * 显示手机验证码2FA
     */
    private void showPhoneOtpDialog(String phoneNumber,String CasCookie,String VPNToken,TokenData tokenData) {
        try {
            AlertDialog dialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setContentView(R.layout.login_phoneotp);
            TextView phoneNumberView = window
                    .findViewById(R.id.et_phone);
            phoneNumberView.setText(phoneNumber);
            Button buttonYes = window.findViewById(R.id.btn_text_yes);
            buttonYes.setOnClickListener(view -> {
                TextView phoneOTP = window
                        .findViewById(R.id.et_phoneotp);
                String OTP=phoneOTP.getText().toString();
                dialog.dismiss();
                testCASWithOTP(OTP,CasCookie,VPNToken,tokenData);
            });
        } catch (Exception ignore) {
            return;
        }
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
        TokenData tokenData = TokenData.newInstance(this);
        runOnUiThread(() -> button.setText("正在登录"));
        tokenData.refresh();
        runOnUiThread(() -> button.setText("获取个人信息"));
        StudentInfo studentInfo = null;
        try {
            studentInfo = StaticService.getStudentInfo(this, tokenData.getCookie());
            if (!generalData.isInternational()) {
                List<TermBean> allTerm = StaticService.getTerms(this, tokenData.getCookie());
                if (allTerm != null) {
                    MoreDate.newInstance(this).setTermBeans(allTerm);
                }
            }
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
     * 设置按钮可点击
     */
    private void setEnClick() {
        button.setText("登录");
        button.setEnabled(true);
        etAccount.setEnabled(true);
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
        DialogUtil.setTextDialog(this, getContext().getResources().getString(R.string.log_change_pwd), "好的", iDialogService, false);
    }

    public void showHelp(View view) {
        DialogUtil.showTextDialog(this, getContext().getResources().getString(R.string.login_help));
    }

    public void activateAccount(View view) {
        openUrl(getContext().getResources().getString(R.string.url_activate_account));
    }
}
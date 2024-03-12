package top.yvyan.guettable.activity;

import static com.xuexiang.xui.XUI.getContext;

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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class LoginActivity extends Activity implements View.OnClickListener {
    public static int REQUEST_CODE = 13;
    public static int OK = 10;

    private static final String AUTO_TERM = "login_auto_term";

    private Boolean bPwdSwitch = false;
    private Boolean bPwdSwitch2 = false;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private CheckBox cbAutoTerm;
    private SuperButton button;
    private EditText etPwd;
    private ImageView ivPwdSwitch;
    private MiniLoadingDialog mMiniLoadingDialog;

    private AccountData accountData;
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountData = AccountData.newInstance(getContext());
        mmkv = MMKV.defaultMMKV();

        mMiniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this);
        ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        button = findViewById(R.id.login);
        button.setOnClickListener(this);
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        cbRememberPwd.setChecked(true);
        cbAutoTerm = findViewById(R.id.cb_auto_term);
        cbAutoTerm.setChecked(mmkv.decodeBool(AUTO_TERM, true));
        ivPwdSwitch.setOnClickListener(showPwdClickListener());
        TextView profileVersion = findViewById(R.id.tv_profile_version);
        profileVersion.setText(AppUtil.getAppVersionName(Objects.requireNonNull(getContext())));
        //获取账号密码
        if (accountData.getIsSave()) {
            etAccount.setText(accountData.getUsername());
            etPwd.setText(accountData.getPwd());
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
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                ivPwdSwitch.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                etPwd.setTypeface(Typeface.DEFAULT);
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
        new Thread(() -> testCAS(account, pwd)).start();
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
     * 验证智慧校园密码
     *
     * @param account  学号
     * @param password 密码
     */
    private void testCAS(String account, String password) {
        new Thread(() -> {
            TokenData tokenData = TokenData.newInstance(this);
            runOnUiThread(() -> {
                button.setText("正在认证");
                setUnClick();
            });
            accountData.setUser(account, password, cbRememberPwd.isChecked());
            int authState = tokenData.refreshTGT(()->{
                getInfo();
            });
            switch (authState) {
                case 0:
                    getInfo();
                    break;
                case -1:
                    showErrorToast(-4);
                    break;
                case -3:
                    // reAuth, do nothing
                    break;
                default:
                    runOnUiThread(() -> {
                        accountData.logoff();
                        setEnClick();
                    });
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
        TokenData tokenData = TokenData.newInstance(this);
        runOnUiThread(() -> button.setText("正在登录"));
        tokenData.refresh();
        runOnUiThread(() -> button.setText("获取个人信息"));
        StudentInfo studentInfo = null;
        try {
            studentInfo = StaticService.getStudentInfo(this, tokenData.getBkjwCookie());

            List<TermBean> allTerm = StaticService.getTerms(this, tokenData.getBkjwCookie());
            if (allTerm != null) {
                MoreData.setTermBeans(allTerm);
            }
        } catch (Exception ignored) {
        }

        if (studentInfo != null) {
            //保存个人基础信息
            GeneralData generalData = GeneralData.newInstance(this);
            generalData.setNumber(studentInfo.getStid());
            generalData.setName(studentInfo.getName());
            generalData.setTerm(studentInfo.getTerm());
            generalData.setGrade(studentInfo.getGrade());
            Intent intent = new Intent(this, SetTermActivity.class);
            //保存自动学期设定的状态
            if (cbAutoTerm.isChecked()) {
                intent.putExtra("auto", true);
                mmkv.encode(AUTO_TERM, true);
            } else {
                intent.putExtra("auto", false);
                mmkv.encode(AUTO_TERM, false);
            }
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
        etPwd.setEnabled(true);
        mMiniLoadingDialog.dismiss();
    }

    /**
     * 设置按钮不可点击
     */
    private void setUnClick() {
        button.setText("网络初始化");
        button.setEnabled(false);
        etAccount.setEnabled(false);
        etPwd.setEnabled(false);
        mMiniLoadingDialog.updateMessage("正在登录...");
        mMiniLoadingDialog.show();
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
        //DialogUtil.showTextDialog(this, getContext().getResources().getString(R.string.login_help));
    }

    public void activateAccount(View view) {
        openUrl(getContext().getResources().getString(R.string.url_activate_account));
    }

    public void openCampus(View view) {
        openUrl(getContext().getResources().getString(R.string.url_smart_campus));
    }
}
package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.*;

import androidx.appcompat.app.AlertDialog;

import top.yvyan.guettable.R;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class TokenData {
    @SuppressLint("StaticFieldLeak")
    private static TokenData tokenData;
    private final SharedPreferences.Editor editor;
    private Context context;

    private Object Caller;

    private static final String SHP_NAME = "tokenData";

    private static final String CAS_TGTToken = "TGTToken";

    private static final String VPN_TOKEN = "VPNToken";
    private static final String BKJW_COOKIE = "bkjwCookie";

    private static final String BKJW_TEST_COOKIE = "bkjwTestCookie";
    private static final String IS_DEVELOP = "isDevelop";
    private static final String MULTIFACTOR_USERS = "MFACookie";
    private final AccountData accountData;

    private static boolean isVPN = true;

    //开发者调试
    private boolean isDevelop;
    //强制获取vpn
    private boolean forceVPN = false;

    private String TGTToken; // CAS-TGT
    private String VPNToken;   //VPN认证Token
    private String bkjwTestCookie; //新教务系统认证Cookie
    private String bkjwCookie; //教务系统认证Cookie

    private String MFACookie;

    public String getCookie() {
        if (isVPN) {
            return VPNToken;
        } else {
            return bkjwCookie;
        }
    }

    public String getbkjwTestCookie() {
        if (isVPN) {
            return VPNToken;
        } else {
            return bkjwTestCookie;
        }
    }

    public static boolean isVPN() {
        return isVPN;
    }

    public static void setIsVPN(boolean isVPN) {
        TokenData.isVPN = isVPN;
    }

    /**
     * 获取最新的VPNToken
     *
     * @return VPNToken
     */
    public String getVpnToken() {
        forceVPN = true;
        //isVPN = Net.testNet(context) != 0;
        return VPNToken;
    }

    public String getCASCookie() {
        return TGTToken + (MFACookie == "" ? "" : "; " + MFACookie);
    }

    @SuppressLint("CommitPrefEdits")
    private TokenData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        accountData = AccountData.newInstance(context);
        this.context = context;

        VPNToken = sharedPreferences.getString(VPN_TOKEN, null);
        bkjwCookie = sharedPreferences.getString(BKJW_COOKIE, null);
        bkjwTestCookie = sharedPreferences.getString(BKJW_TEST_COOKIE, null);
        isDevelop = sharedPreferences.getBoolean(IS_DEVELOP, false);
        TGTToken = sharedPreferences.getString(CAS_TGTToken, null);
        MFACookie = sharedPreferences.getString(MULTIFACTOR_USERS, null);

    }

    public static TokenData newInstance(Context context) {
        if (tokenData == null) {
            tokenData = new TokenData(context);
        }
        tokenData.context = context; // Update Context
        tokenData.Caller = null;
        return tokenData;
    }

    public static TokenData newInstance(Context context, Object Caller) {
        if (tokenData == null) {
            tokenData = new TokenData(context);
        }
        tokenData.context = context; // Update Context
        tokenData.Caller = Caller;
        return tokenData;
    }

    /**
     * 刷新登录凭证
     *
     * @return state记录当前状态
     * 0 : 登录成功
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * 2 : 未登录
     */
    public int refresh() {
        if (isDevelop) { //调试模式不刷新凭证
            return 0;
        }
        if (accountData.getIsLogin()) {
            if (forceVPN) {
                isVPN = true;
                forceVPN = false;
            } else {
                isVPN = Net.testNet() != 200;
            }
            return loginBySmart();
        } else {
            return 2;
        }
    }

    /**
     * 使用智慧校园登录
     *
     * @return 登录结果
     */
    private int loginBySmart() {
        //尝试获取教务系统ST
        if (isVPN) { //外网
            //获取VPN的token
            String VPNTokenStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
            if (VPNTokenStr.startsWith("ERROR")) {
                if (VPNTokenStr == "ERRORNeedlogin") {
                    int n;
                    if ((n = refreshTGT()) != 0) {
                        return n;
                    }
                    ;
                    VPNTokenStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
                    if (VPNTokenStr.startsWith("ERROR")) {
                        return -2;
                    }
                }
            }
            if (VPNTokenStr != null) {
                setVPNToken(VPNTokenStr);
            }

            String bkjwCookie = StaticService.authServiceByCas(context, "https://bkjw.guet.edu.cn", getCASCookie(), VPNTokenStr, isVPN);
            if (bkjwCookie.startsWith("ERROR")) {
                return -2;
            }

            String bkjwTestCookie = StaticService.authServiceByCas(context, "https://bkjwtest.guet.edu.cn/student/sso/login", getCASCookie(), VPNTokenStr, isVPN);
            if (bkjwTestCookie.startsWith("ERROR")) {
                return -2;
            }

            return 0;

        } else { //内网
            String BkjwCookieStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
            if (BkjwCookieStr.startsWith("ERROR")) {
                if (BkjwCookieStr == "ERRORNeedlogin") {
                    int n;
                    if ((n = refreshTGT()) != 0) {
                        return n;
                    }
                    ;
                    BkjwCookieStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
                    if (BkjwCookieStr.startsWith("ERROR")) {
                        return -2;
                    }
                }
            }
            if (BkjwCookieStr != null) {
                setBkjwCookie(BkjwCookieStr);
            }
            String BkjwTestStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
            if (BkjwTestStr.startsWith("ERROR")) {
                return -2;
            }
            if (BkjwTestStr != null) {
                setBkjwTestCookie(BkjwTestStr);
                return 0;
            }
            return -2;
        }
    }

    /**
     * 刷新TGT令牌
     *
     * @return 操作结果
     */
    public int refreshTGT() {
        String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getPwd(), TGTToken, MFACookie);
        if (TGTTokenStr.equals("ERROR2") || TGTTokenStr.equals("ERROR0")) {
            return -2;
        }
        if (TGTTokenStr.contains("TGT-")) {
            if (TGTTokenStr.contains("ERROR5")) {
                String CASCookie = TGTTokenStr.substring(TGTTokenStr.indexOf(";") + 1);
                setTGTToken(CASCookie);
                Activity activity = this.getActivity(context);
                if (activity == null) return -3;
                String phoneNumber = StaticService.reAuth_sendSMSCode(context, accountData.getUsername(), CASCookie);
                if (!phoneNumber.contains("ERROR")) {
                    reAuth_showSMSCodeDialog(activity, phoneNumber, CASCookie);
                } else {
                    if (phoneNumber.contains("ERROR3")) {
                        activity.runOnUiThread(() -> {
                            ToastUtil.showToast(context, context.getResources().getString(R.string.login_fail_SMSCodeSend) + phoneNumber.substring(7));
                        });
                    } else {
                        activity.runOnUiThread(() -> {
                            ToastUtil.showToast(context, context.getResources().getString(R.string.login_fail_SMSCodeSend) + "未知错误");
                        });
                    }
                }
                return -3;
            } else {
                setTGTToken(TGTTokenStr);
            }
            return 0;
        } else {
            return -1;
        }
    }

    public Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    private void reAuth_showSMSCodeDialog(Activity activity, String phoneNumber, String CasCookie) {
        activity.runOnUiThread(() -> {
            try {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setContentView(R.layout.login_smscode);
                TextView phoneNumberView = window
                        .findViewById(R.id.et_phone);
                phoneNumberView.setText(phoneNumber);
                Button buttonYes = window.findViewById(R.id.btn_text_yes);
                Button buttonCancel = window.findViewById(R.id.btn_text_cancel);
                buttonCancel.setOnClickListener(view -> {
                    dialog.cancel();
                });
                buttonYes.setOnClickListener(view -> {
                    TextView SMSCode = window
                            .findViewById(R.id.et_smscode);
                    String SMSCodeOTP = SMSCode.getText().toString();
                    buttonYes.setText("正在登录");
                    buttonYes.setEnabled(false);
                    new Thread(() -> {
                        int State = reAuth_SMSCode(SMSCodeOTP, CasCookie);
                        activity.runOnUiThread(() -> {
                            if (State == 0) {
                                dialog.dismiss();
                            } else {
                                buttonYes.setText("登录");
                                buttonYes.setEnabled(true);
                            }
                            ToastUtil.showToast(activity, State == 0 ? "验证成功" : "验证码有误");
                        });
                    }).start();
                });
            } catch (Exception ignore) {

            }
        });
    }

    private int reAuth_SMSCode(String SMSCode, String CASCookie) {
        try {
            String MultiFactorAuth = StaticService.reAuth_SMSCode(context, SMSCode, CASCookie);
            if (MultiFactorAuth.contains("ERROR")) {
                return -1;
            } else {
                setMFACookie(MultiFactorAuth);
                if (Caller != null) {
                    try {
                        // 反射尝试调用Update方法 (如果有)
                        Class ContextClass = Caller.getClass();
                        Method Update = ContextClass.getMethod("update");
                        Update.invoke(Caller);
                    } catch (Exception ignore) {

                    }
                }
                return 0;
            }
        } catch (Exception ignore) {
            return -1;
        }
    }


    public int setVPNCASCookie() {
        return (MFACookie != null ? StaticService.CookieSet(context, "cas.guet.edu.cn", "/authserver/login", MFACookie, VPNToken) : 0) | StaticService.CookieSet(context, "cas.guet.edu.cn", "/authserver/login", TGTToken, VPNToken);
    }

    public String getTGTToken() {
        return TGTToken;
    }

    public String getMFACookie() {
        return MFACookie;
    }

    public void setTGTToken(String CASCookie) {
        String tTGTToken = CASCookie.substring(CASCookie.indexOf("CASTGC="));
        int TGTTokenEndIndex = tTGTToken.indexOf(";");
        if (TGTTokenEndIndex >= 0) {
            TGTToken = tTGTToken.substring(0, TGTTokenEndIndex);
        } else {
            TGTToken = tTGTToken;
        }
        editor.putString(CAS_TGTToken, TGTToken);
        editor.apply();
    }

    public void setMFACookie(String MFACookie) {
        this.MFACookie = MFACookie;
        editor.putString(MULTIFACTOR_USERS, MFACookie);
        editor.apply();
    }

    public void setVPNToken(String VPNToken) {
        this.VPNToken = VPNToken;
        editor.putString(VPN_TOKEN, VPNToken);
        editor.apply();
    }

    public void setBkjwCookie(String bkjwCookie) {
        this.bkjwCookie = bkjwCookie;
        editor.putString(BKJW_COOKIE, bkjwCookie);
        editor.apply();
    }

    public void setBkjwTestCookie(String bkjwCookie) {
        this.bkjwCookie = bkjwCookie;
        editor.putString(BKJW_TEST_COOKIE, bkjwCookie);
        editor.apply();
    }

    public boolean isDevelop() {
        return isDevelop;
    }

    public void setDevelop(boolean develop) {
        isDevelop = develop;
        editor.putBoolean(IS_DEVELOP, isDevelop);
        editor.apply();
    }
}

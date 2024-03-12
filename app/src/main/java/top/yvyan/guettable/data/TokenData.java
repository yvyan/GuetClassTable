package top.yvyan.guettable.data;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Consumer;
import androidx.core.util.Supplier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import top.yvyan.guettable.R;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.SSLUtils;
import top.yvyan.guettable.util.ToastUtil;

public class TokenData {
    @SuppressLint("StaticFieldLeak")
    private static TokenData tokenData;
    private final SharedPreferences.Editor editor;
    private Context context;
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

    @SafeVarargs
    /**
     * tryUpdate 方法，顺序执行需要账户认证的同步流程并自动处理登录失效后的重新登录
     * @param updateFunction 同步路径函数，返回是否同步成功
     * @return
     */
    public synchronized final boolean tryUpdate(Supplier<Boolean>... updateFunction) {
        return tryUpdate(null,updateFunction);
    }

    /**
     * tryUpdate 方法，顺序执行需要账户认证的同步流程并自动处理登录失效后的重新登录
     * @param reloginHint 重新登录的提示
     * @param updateFunction 同步路径函数，返回是否同步成功
     * @return
     */
    @SafeVarargs
    public synchronized final boolean tryUpdate(Runnable reloginHint, @NonNull Supplier<Boolean>... updateFunction) {
        boolean relogined = false;
        for(int i=0; i<updateFunction.length; i++) {
            Supplier<Boolean> updateMethod = updateFunction[i];
            boolean success;
            try {
                success=updateMethod.get();
            } catch (Exception setRelogin) {
                success=false;
            }
            if(!success) {
                if(!relogined) {
                    relogined=true;
                    if(reloginHint != null) {
                        reloginHint.run();
                    }
                    if(refresh()==-3) {
                        try {
                            this.wait();
                        } catch (Exception ignore) {
                        }
                        loginBySmart(); // auth Services
                    }
                    i--;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public String getBkjwCookie() {
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
        return TGTToken + (MFACookie==null || MFACookie.isEmpty() ? "" : "; " + MFACookie);
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
        return tokenData;
    }

    public int refresh() {
        return refresh(null);
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
    public int refresh(Supplier<Void> callback) {
        this.tgtCallback=callback;
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
                if (VPNTokenStr.equals("ERRORNeedlogin")) {
                    int n;
                    if ((n = refreshTGT()) != 0) {
                        return n;
                    }
                    VPNTokenStr = StaticService.authServiceByCas(context, "https://v.guet.edu.cn/login?cas_login=true", getCASCookie(), "", isVPN);
                    if (VPNTokenStr.startsWith("ERROR")) {
                        return -2;
                    }
                }
            }
            setVPNToken(VPNTokenStr);

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
            String BkjwCookieStr = StaticService.authServiceByCas(context, "https://bkjw.guet.edu.cn", getCASCookie(), "", isVPN);
            if (BkjwCookieStr.startsWith("ERROR")) {
                if (BkjwCookieStr.equals("ERRORNeedlogin")) {
                    int n;
                    if ((n = refreshTGT()) != 0) {
                        return n;
                    }
                    BkjwCookieStr = StaticService.authServiceByCas(context, "https://bkjw.guet.edu.cn", getCASCookie(), "", isVPN);
                    if (BkjwCookieStr.startsWith("ERROR")) {
                        return -2;
                    }
                }
            }
            setBkjwCookie(BkjwCookieStr);
            String BkjwTestStr = StaticService.authServiceByCas(context, "https://bkjwtest.guet.edu.cn/student/sso/login", getCASCookie(), "", isVPN);
            if (BkjwTestStr.startsWith("ERROR")) {
                return -2;
            }
            setBkjwTestCookie(BkjwTestStr);
            return 0;
        }
    }

    private Supplier<Void> tgtCallback;

    public int refreshTGT() {
        return refreshTGT(null);
    }

    /**
     *
     * @param Callback 当存在二步验证等需用户交互的流程完成时调用的函数;
     * @return
     */
    public final int refreshTGT(Supplier<Void> Callback) {
        tgtCallback=Callback;
        boolean checkCaptcha = StaticService.checkNeedCaptcha(context, accountData.getUsername());
        if (checkCaptcha) {
            Activity activity = this.getActivity(context);
            login_showCaptchaDialog(activity);
            return -3;
        } else {
            return refreshTGTWithCaptcha("", "");
        }
    }

    /**
     * 刷新TGT令牌
     *
     * @return 操作结果
     */
    public synchronized int refreshTGTWithCaptcha(String Captcha, String SessionCookie) {
        String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getPwd(), Captcha, TGTToken, MFACookie, SessionCookie);
        if (TGTTokenStr.equals("ERROR2") || TGTTokenStr.equals("ERROR0")) {
            return -2;
        }
        if (TGTTokenStr.contains("TGT-")) {
            if (TGTTokenStr.contains("ERROR5")) {
                String CASCookie = TGTTokenStr.substring(TGTTokenStr.indexOf(";") + 1);
                setTGTToken(CASCookie);
                Activity activity = this.getActivity(context);
                if (activity == null) return -5;
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
                if (Captcha != null && !Captcha.isEmpty()) {
                    if(tgtCallback!=null) {
                        try {
                            new Thread(()->{
                                tgtCallback.get();
                                tgtCallback=null;
                            }).start();
                        } catch (Exception ignored) {

                        }
                    }
                    try {
                        this.notify();
                    } catch (Exception ignored) {}
                }
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

    private void login_showCaptchaDialog(Activity activity) {
        activity.runOnUiThread(() -> {
            try {
                AtomicReference<String> CasCookieWithSession = new AtomicReference<>();
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setContentView(R.layout.login_captcha);
                ImageView captchaView = window
                        .findViewById(R.id.img_captcha);
                captchaView.setOnClickListener(view -> {
                    new Thread(() -> {
                        try {
                            URL url = new URL("https://cas.guet.edu.cn/authserver/getCaptcha.htl?" + System.currentTimeMillis());
                            HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
                            urlConn.setUseCaches(false);
                            SSLContext sslContext = SSLUtils.getSSLContextWithoutCer();
                            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                            urlConn.setSSLSocketFactory(sslSocketFactory);
                            urlConn.setHostnameVerifier(SSLUtils.hostnameVerifier);
                            urlConn.connect();
                            InputStream in = urlConn.getInputStream();
                            Bitmap captchaImg = BitmapFactory.decodeStream(in);
                            activity.runOnUiThread(() -> {
                                captchaView.setImageBitmap(captchaImg);
                            });
                            //get cookie from server
                            String set_cookie = null;
                            StringBuilder cookie_builder = new StringBuilder();
                            List<String> cookies = urlConn.getHeaderFields().get("Set-Cookie");
                            if (cookies != null) {
                                for (String cookie_resp : cookies) {
                                    cookie_builder.append(cookie_resp.substring(0, cookie_resp.indexOf(";") + 1) + " ");
                                }
                            }
                            set_cookie = cookie_builder.length() == 0 ? "" : cookie_builder.substring(0, max(0, cookie_builder.length() - 2));
                            CasCookieWithSession.set(set_cookie);
                        } catch (Exception ignore) {

                        }
                    }).start();
                });
                captchaView.setClickable(true);
                captchaView.performClick();
                Button buttonYes = window.findViewById(R.id.btn_text_yes);
                Button buttonCancel = window.findViewById(R.id.btn_text_cancel);
                buttonCancel.setOnClickListener(view -> {
                    dialog.cancel();
                });
                buttonYes.setOnClickListener(view -> {
                    TextView Captcha = window
                            .findViewById(R.id.et_captcha);
                    String captcha = Captcha.getText().toString();
                    if (captcha.isEmpty()) {
                        ToastUtil.showToast(activity, "验证码不能为空!");
                        return;
                    }
                    dialog.dismiss();
                    new Thread(() -> {
                        refreshTGTWithCaptcha(captcha, CasCookieWithSession.get());
                    }).start();
                });
            } catch (Exception ignore) {

            }
        });
    }

    private synchronized void reAuth_showSMSCodeDialog(Activity activity, String phoneNumber, String CasCookie) {
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

    private synchronized int reAuth_SMSCode(String SMSCode, String CASCookie) {
        try {
            String MultiFactorAuth = StaticService.reAuth_SMSCode(context, SMSCode, CASCookie);
            if (MultiFactorAuth.contains("ERROR")) {
                return -1;
            } else {
                setMFACookie(MultiFactorAuth);
                try {
                    this.notify();
                } catch (Exception ignore) {
                }
                if(tgtCallback!=null) {
                    try {
                        new Thread(()->{
                            tgtCallback.get();
                            tgtCallback=null;
                        }).start();
                    } catch (Exception ignored) {

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

    public String extractCookie(String source, String Cookie) {
        String output;
        int CookieStart = source.indexOf(Cookie + "=");
        if (CookieStart < 0) {
            return source;
        }
        String substr = source.substring(CookieStart);
        int CookieEndIndex = substr.indexOf(";");
        if (CookieEndIndex >= 0) {
            output = substr.substring(0, CookieEndIndex);
        } else {
            output = substr;
        }
        return output;
    }

    public void setTGTToken(String CASCookie) {
        TGTToken = extractCookie(CASCookie, "CASTGC");
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
        this.bkjwTestCookie = bkjwCookie;
        editor.putString(BKJW_TEST_COOKIE, bkjwTestCookie);
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

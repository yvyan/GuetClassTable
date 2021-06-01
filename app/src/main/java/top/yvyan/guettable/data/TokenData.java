package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import top.yvyan.guettable.R;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;

public class TokenData {
    @SuppressLint("StaticFieldLeak")
    private static TokenData tokenData;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final String SHP_NAME = "tokenData";
    private static final String LOGIN_TYPE = "loginType";
    private static final String TGT_TOKEN = "TGTToken";
    private static final String VPN_TOKEN = "VPNToken";
    private static final String BKJW_COOKIE = "bkjwCookie";
    private static final String IS_DEVELOP = "isDevelop";

    private final AccountData accountData;

    private int loginType; //0 : VPN + CAS登录;  1 : VPN + 教务登录
    public static boolean isVPN = true;

    //开发者调试
    private boolean isDevelop;
    //强制获取vpn
    private boolean forceVPN = false;

    private String TGTToken;   //统一登录TGT令牌
    private String VPNToken;   //VPN认证Token
    private String bkjwCookie; //教务系统认证Cookie

    public String getCookie() {
        if (isVPN) {
            return VPNToken;
        } else {
            return bkjwCookie;
        }
    }

    /**
     * 获取最新的VPNToken
     *
     * @return VPNToken
     */
    public String getVpnToken() {
        forceVPN = true;
        refresh();
        //isVPN = Net.testNet(context) != 0;
        return VPNToken;
    }

    @SuppressLint("CommitPrefEdits")
    private TokenData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        accountData = AccountData.newInstance(context);
        this.context = context;

        loginType = sharedPreferences.getInt(LOGIN_TYPE, 1);
        TGTToken = sharedPreferences.getString(TGT_TOKEN, "TGT-");
        VPNToken = sharedPreferences.getString(VPN_TOKEN, null);
        bkjwCookie = sharedPreferences.getString(BKJW_COOKIE, null);
        isDevelop = sharedPreferences.getBoolean(IS_DEVELOP, false);
    }

    public static TokenData newInstance(Context context) {
        if (tokenData == null) {
            tokenData = new TokenData(context);
        }
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
                isVPN = Net.testNet(context) != 0;
            }
            if (loginType == 0) {
                if (isVPN) { //外网
                    String VPNTokenStr = Net.getVPNToken(context);
                    if (VPNTokenStr != null) {
                        setVPNToken(VPNTokenStr);
                    } else {
                        return -2;
                    }
                    int n = StaticService.loginVPN(context, VPNToken, accountData.getUsername(), accountData.getVPNPwd());
                    //登录教务
                    if (n == 0) {
                        String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), VPNTokenStr);
                        if (ST_BKJW.equals("ERROR0")) {
                            return -2;
                        } else if (ST_BKJW.equals("ERROR1")) { //TGT失效
                            n = refreshTGT(VPNTokenStr);
                            if (n == 0) {
                                ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), VPNTokenStr);
                                if (!ST_BKJW.contains("ST-")) {
                                    return -2;
                                }
                            } else {
                                return n;
                            }
                        }
                        n = StaticService.loginBkjwVPNST(ST_BKJW, VPNToken);
                        if (n != 0) {
                            n = StaticService.loginBkjwVPNST(ST_BKJW, VPNToken);
                        }
                    }
                    return n;
                } else { // 内网
                    StringBuilder cookie_builder = new StringBuilder();
                    String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), null);
                    if (!ST_BKJW.contains("ST-")) { // TGT失效
                        int n = refreshTGT(null);
                        if (n != 0) {
                            return n;
                        }
                        ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), null);
                        if (!ST_BKJW.contains("ST-")) { // 网络错误，切换为外网模式
                            return -2;
                        }
                    }
                    int state = StaticService.loginBkjw(context, ST_BKJW, cookie_builder);
                    if (state == 0) {
                        setBkjwCookie(cookie_builder.toString());
                        return 0;
                    } else {
                        return state;
                    }
                }
            } else if (loginType == 1) { //VPN+教务登录(目前只有内网)
                if (isVPN) {
                    String VPNTokenStr = Net.getVPNToken(context);
                    if (VPNTokenStr != null) {
                        setVPNToken(VPNTokenStr);
                    } else {
                        return -2;
                    }
                    int n = StaticService.loginVPN(context, VPNToken, accountData.getUsername(), accountData.getVPNPwd());
                    if (n == 0) {
                        n = StaticService.autoLoginV(context, accountData.getUsername(), accountData.getBkjwPwd(), VPNToken);
                    }
                    return n;
                } else {
                    StringBuilder cookie_builder = new StringBuilder();
                    int state = StaticService.autoLogin(
                            context,
                            accountData.getUsername(),
                            accountData.getBkjwPwd(),
                            cookie_builder
                    );
                    if (state == 0) {
                        setBkjwCookie(cookie_builder.toString());
                        return 0;
                    } else {
                        return state;
                    }
                }
            } else {
                return 2;
            }
        } else {
            return 2;
        }
    }

    /**
     * 刷新TGT令牌
     *
     * @param VPNToken VPNToken
     * @return 操作结果
     */
    public int refreshTGT(String VPNToken) {
        String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getVPNPwd(), VPNToken);
        if (TGTTokenStr.equals("ERROR2")) {
            return -2;
        }
        if (TGTTokenStr.contains("TGT-")) {
            setTGTToken(TGTTokenStr);
            return 0;
        } else {
            return -1;
        }
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
        editor.putInt(LOGIN_TYPE, loginType);
        editor.apply();
    }

    public void setTGTToken(String TGTToken) {
        this.TGTToken = TGTToken;
        editor.putString(TGT_TOKEN, TGTToken);
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

    public boolean isDevelop() {
        return isDevelop;
    }

    public void setDevelop(boolean develop) {
        isDevelop = develop;
        editor.putBoolean(IS_DEVELOP, isDevelop);
        editor.apply();
    }
}

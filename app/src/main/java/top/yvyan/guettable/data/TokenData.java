package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import top.yvyan.guettable.R;
import top.yvyan.guettable.service.fetch.Net;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class TokenData {
    @SuppressLint("StaticFieldLeak")
    private static TokenData tokenData;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final String SHP_NAME = "tokenData";

    private static final String CAS_Cookie = "CASCookie";
    private static final String VPN_TOKEN = "VPNToken";
    private static final String BKJW_COOKIE = "bkjwCookie";
    private static final String IS_DEVELOP = "isDevelop";

    private final AccountData accountData;

    public static boolean isVPN = true;

    //开发者调试
    private boolean isDevelop;
    //强制获取vpn
    private boolean forceVPN = false;

    private String CASCookie; // 新版CAS认证Cookie; CASTGT/JSESSION
    private String VPNToken;   //VPN认证Token
    private String bkjwCookie; //教务系统认证Cookie

    public String getCookie() {
        if (isVPN) {
            return VPNToken;
        } else {
            return bkjwCookie;
        }
    }

    public boolean isVPN() {
        return !isVPN;
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

        VPNToken = sharedPreferences.getString(VPN_TOKEN, null);
        bkjwCookie = sharedPreferences.getString(BKJW_COOKIE, null);
        isDevelop = sharedPreferences.getBoolean(IS_DEVELOP, false);
        CASCookie = sharedPreferences.getString(CAS_Cookie, "");
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
        if (isVPN) { //外网
            //获取VPN的token
            String VPNTokenStr = Net.getVPNToken(context);
            if (VPNTokenStr != null) { //保存token
                setVPNToken(VPNTokenStr);
            } else {
                return -2;
            }
            int n = loginVpnByCAS(VPNTokenStr);
            //登录教务
            if (n == 0) {
                String ST_BKJW = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_bkjw), VPNTokenStr);
                if (ST_BKJW.equals("ERROR0")) {
                    return -2;
                } else if (ST_BKJW.equals("ERROR1")) { //TGT失效
                    n = refreshTGT(VPNTokenStr);
                    if (n == 0) {
                        ST_BKJW = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_bkjw), VPNTokenStr);
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
            String ST_BKJW = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_bkjw), null);
            if (!ST_BKJW.contains("ST-")) { // TGT失效
                int n = refreshTGT(null);
                if (n != 0) {
                    return n;
                }
                ST_BKJW = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_bkjw), null);
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
    }

    /**
     * 使用CAS登录VPN
     *
     * @param VPNTokenStr vpn的Token
     * @return 登录结果
     */
    private int loginVpnByCAS(String VPNTokenStr) {
        int n;
        String ST_VPN = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_vpn), VPNTokenStr);
        if (ST_VPN.equals("ERROR0")) {
            return -2;
        } else if (ST_VPN.equals("ERROR1")) { //TGT失效
            n = refreshTGT(VPNTokenStr); //刷新TGT
            if (n == 0) {
                //重新获取登录vpn的st令牌
                ST_VPN = StaticService.SSOGetST(context, CASCookie, context.getResources().getString(R.string.service_vpn), VPNTokenStr);
                if (!ST_VPN.contains("ST-")) {
                    return -2;
                }
                if (StaticService.loginVPNST(ST_VPN, VPNTokenStr) != 0) {
                    return StaticService.loginVPNST(ST_VPN, VPNTokenStr);
                }
                return 0;
            } else {
                return n;
            }
        } else {
            if (StaticService.loginVPNST(ST_VPN, VPNTokenStr) != 0) {
                return StaticService.loginVPNST(ST_VPN, VPNTokenStr);
            }
            return 0;
        }
    }

    /**
     * 刷新TGT令牌
     *
     * @param VPNToken VPNToken
     * @return 操作结果
     */
    public int refreshTGT(String VPNToken) {
        String CASCookieStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getVPNPwd(), VPNToken);
        if (CASCookieStr.equals("ERROR2") || CASCookieStr.equals("ERROR0")) {
            return -2;
        }
        if (CASCookieStr.contains("TGT-")) {
            if(CASCookieStr.contains("ERROR5")) {
                setCASCookie(CASCookieStr.substring(CASCookieStr.indexOf(";")+1));
               return fuck2FA(accountData.getUsername(),accountData.getVPNPwd(),CASCookieStr.substring(CASCookieStr.indexOf(";")+1),VPNToken);
            } else {
                setCASCookie(CASCookieStr);
            }
            return 0;
        } else {
            return -1;
        }
    }

    private int fuck2FA(String account,String password,String CASCookie,String VPNToken) {
        try{
            String MulitFactorAuth = StaticService.fuck2FA(context,password,CASCookie,VPNToken);
            if(MulitFactorAuth.contains("ERROR")) {
                return -1;
            } else {
                setCASCookie(CASCookie + "; " + MulitFactorAuth);
            }
            return 0;
        } catch (Exception ignore) {
            return 0;
        }
    }

    public void setCASCookie(String CASCookie) {
        this.CASCookie = CASCookie;
        editor.putString(CAS_Cookie, CASCookie);
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

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

    private static final String CAS_TGTToken = "TGTToken";

    private static final String VPN_TOKEN = "VPNToken";
    private static final String BKJW_COOKIE = "bkjwCookie";
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
    private String bkjwCookie; //教务系统认证Cookie

    private String MFACookie;

    public String getCookie() {
        if (isVPN) {
            return VPNToken;
        } else {
            return bkjwCookie;
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
        TGTToken = sharedPreferences.getString(CAS_TGTToken, "");
        MFACookie = sharedPreferences.getString(MULTIFACTOR_USERS, null);
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
        //尝试获取教务系统ST
        int n;
        String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), MFACookie);
        if (!ST_BKJW.contains("ST-")) { // TGT失效
            n = refreshTGT();
            if (n != 0) {
                return n;
            }
            ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), MFACookie);
            if (!ST_BKJW.contains("ST-")) {
                return -2;
            }
        }

        if (isVPN) { //外网
            //获取VPN的token
            String VPNTokenStr = Net.getVPNToken(context);
            if (VPNTokenStr != null) { //保存token
                setVPNToken(VPNTokenStr);
            } else {
                return -2;
            }
            //获取VPN-ST
            String ST_VPN = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_vpn), MFACookie);
            if (!ST_VPN.contains("ST-")) {
                return -2;
            }
            n = StaticService.loginVPNST(context, ST_VPN, VPNTokenStr);
            if (n != 0) {
                n = StaticService.loginVPNST(context, ST_VPN, VPNTokenStr);
            }
            if (n != 0) {
                return n;
            }
            n = StaticService.loginBkjwVPNST(context, ST_BKJW, VPNToken);
            if (n != 0) {
                n = StaticService.loginBkjwVPNST(context, ST_BKJW, VPNToken);
            }
            return n;

        } else { //内网
            StringBuilder cookie_builder = new StringBuilder();
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
     * 刷新TGT令牌
     *
     * @return 操作结果
     */
    public int refreshTGT() {
        String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getVPNPwd(), MFACookie);
        if (TGTTokenStr.equals("ERROR2") || TGTTokenStr.equals("ERROR0")) {
            return -2;
        }
        if (TGTTokenStr.contains("TGT-")) {
            if (TGTTokenStr.contains("ERROR5")) {
                setTGTToken(TGTTokenStr.substring(TGTTokenStr.indexOf(";") + 1));
                return bypass2FA(accountData.getVPNPwd(), TGTTokenStr.substring(TGTTokenStr.indexOf(";") + 1));
            } else {
                setTGTToken(TGTTokenStr);
            }
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * bypass 2FA
     */
    private int bypass2FA(String password, String CASCookie) {
        try {
            String MultiFactorAuth = StaticService.bypass2FA(context, password, CASCookie);
            if (MultiFactorAuth.contains("ERROR")) {
                return -1;
            } else {
                setMFACookie(MultiFactorAuth);
            }
            return 0;
        } catch (Exception ignore) {
            return 0;
        }
    }

    public int setVPNCASCookie() {
        return StaticService.CookieSet(context, "cas.guet.edu.cn", "/authserver/login", MFACookie, VPNToken) | StaticService.CookieSet(context, "cas.guet.edu.cn", "/authserver/login", TGTToken, VPNToken);
    }

    public String getCASCookie() {
        return tokenData.MFACookie + "; " + tokenData.TGTToken;
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

    public boolean isDevelop() {
        return isDevelop;
    }

    public void setDevelop(boolean develop) {
        isDevelop = develop;
        editor.putBoolean(IS_DEVELOP, isDevelop);
        editor.apply();
    }
}

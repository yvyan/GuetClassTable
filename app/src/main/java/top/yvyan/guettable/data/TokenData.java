package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import top.yvyan.guettable.R;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.service.fetch.LAN;

public class TokenData {
    private static TokenData tokenData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String SHP_NAME = "tokenData";
    private static final String TGT_TOKEN = "TGTToken";
    private static final String VPN_TOKEN = "VPNToken";
    private static final String BKJW_COOKIE = "bkjwCookie";

    private AccountData accountData;

    public static boolean isVPN = true;

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

    private TokenData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        accountData = AccountData.newInstance(context);
        this.context = context;

        TGTToken = sharedPreferences.getString(TGT_TOKEN, "TGT-");
        VPNToken = sharedPreferences.getString(VPN_TOKEN, null);
        bkjwCookie = sharedPreferences.getString(BKJW_COOKIE, null);
    }

    public static TokenData newInstance(Context context) {
        if (tokenData == null) {
            tokenData = new TokenData(context);
        }
        return tokenData;
    }

    /**
     * 刷新登录凭证
     * @return state记录当前状态
     *                 0 : 登录成功
     *                -1 : 密码错误
     *                -2 : 网络错误/未知错误
     *                 2 : 未登录
     */
    public int refresh() {
        if (accountData.getIsLogin()) {


            if(LAN.testNet(context) != 0) {
                isVPN = true;
            } else {
                isVPN = false;
            }
            Log.d("1586", isVPN + "");
            if (isVPN) { //外网
                String VPNTokenStr = LAN.getVPNToken(context);
                if (VPNTokenStr != null) {
                    setVPNToken(VPNTokenStr);
                } else {
                    return -2;
                }
                String ST_VPN = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_vpn), true);
                if (ST_VPN.equals("ERROR0")) {
                    return -2;
                } else if (ST_VPN.equals("ERROR1")) { //TGT失效
                    int n = refreshTGT(true);
                    if (n == 0) {
                        ST_VPN = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_vpn), true);
                        if (!ST_VPN.contains("ST-")) {
                            return -2;
                        }
                    } else {
                        return n;
                    }
                }
                //登录教务
                String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), true);
                if (ST_BKJW.contains("ST-")) {
                    if (StaticService.loginVPN(ST_VPN, VPNToken) != 0) {
                        StaticService.loginVPN(ST_VPN, VPNToken);
                    }
                    int n;
                    n = StaticService.loginBkjwVPN(ST_BKJW, VPNToken);
                    if (n != 0) {
                        n = StaticService.loginBkjwVPN(ST_BKJW, VPNToken);
                    }
                    return n;
                } else {
                    return -2;
                }
            } else { // 内网
                StringBuilder cookie_builder = new StringBuilder();
                String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), false);
                if (!ST_BKJW.contains("ST-")) { // TGT失效
                    int n = refreshTGT(false);
                    if (n != 0) {
                        return n;
                    }
                    ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), false);
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
        } else {
            return 2;
        }
    }

    /**
     * 刷新TGT令牌
     *
     * @param isVPN 是否为外网
     * @return 操作结果
     */
    public int refreshTGT(boolean isVPN) {
        String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getPassword(), isVPN);
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

    public String getTGTToken() {
        return TGTToken;
    }

    public void setTGTToken(String TGTToken) {
        this.TGTToken = TGTToken;
        editor.putString(TGT_TOKEN, TGTToken);
        editor.apply();
    }

    public String getVPNToken() {
        return VPNToken;
    }

    public void setVPNToken(String VPNToken) {
        this.VPNToken = VPNToken;
        editor.putString(VPN_TOKEN, VPNToken);
        editor.apply();
    }

    public String getBkjwCookie() {
        return bkjwCookie;
    }

    public void setBkjwCookie(String bkjwCookie) {
        this.bkjwCookie = bkjwCookie;
        editor.putString(BKJW_COOKIE, bkjwCookie);
        editor.apply();
    }
}

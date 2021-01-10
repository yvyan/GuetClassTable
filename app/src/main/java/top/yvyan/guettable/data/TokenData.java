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

    public static boolean isVPN = false;
    private static boolean isWIFI;

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
     */
    public int refresh() {
        if (accountData.getIsLogin()) {
            if (isVPN) {
                String ST_VPN = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_vpn), isVPN);
                Log.d("1586", "ST:" + ST_VPN);
                if (!ST_VPN.contains("ST-")) { // TGT失效
                    String VPNTokenStr = LAN.getVPNToken(context);
                    setVPNToken(VPNTokenStr);
                    String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getPassword(), isVPN);
                    if (TGTTokenStr.contains("ERROR0")) {
                        return -2;
                    } else if (TGTTokenStr.contains("ERROR1")){
                        return -1;
                    }
                    setTGTToken(TGTTokenStr);
                    // 重新获取ST_VPN
                    ST_VPN = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_vpn), isVPN);
                }
                String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), isVPN);
                if (StaticService.loginVPN(ST_VPN, VPNToken) != 0) {
                    StaticService.loginVPN(ST_VPN, VPNToken);
                }
                int n;
                if (StaticService.loginBkjwVPN(ST_BKJW, VPNToken) != 0) {
                    n = StaticService.loginBkjwVPN(ST_BKJW, VPNToken);
                }
                return 0;
            } else { // 内网
                StringBuilder cookie_builder = new StringBuilder();
                String ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), isVPN);
                Log.d("1586", "ST_BKJW" + ST_BKJW);
                if (!ST_BKJW.contains("ST-")) { // TGT失效
                    String TGTTokenStr = StaticService.SSOLogin(context, accountData.getUsername(), accountData.getPassword(), isVPN);
                    if (TGTTokenStr.contains("ERROR0")) {
                        isVPN = true;
                        deleteToken();
                        return -2;
                    } else if (TGTTokenStr.contains("ERROR1")){
                        return -1;
                    }
                    setTGTToken(TGTTokenStr);
                    ST_BKJW = StaticService.SSOGetST(context, TGTToken, context.getResources().getString(R.string.service_bkjw), isVPN);
                    if (!ST_BKJW.contains("ST-")) { // 网络错误，切换为外网模式
                        isVPN = true;
                        deleteToken();
                        return -8;
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

    public void deleteToken() {
        editor.putString(TGT_TOKEN, null);
        editor.putString(VPN_TOKEN, null);
        editor.putString(BKJW_COOKIE,  null);
        editor.apply();
    }
}

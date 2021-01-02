package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

import top.yvyan.guettable.service.StaticService;

public class TokenData {
    private static TokenData tokenData;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String SHP_NAME = "tokenData";
    private static final String TGT_TOKEN = "TGTToken";
    private static final String VPN_SESSION = "VPNSession";
    private static final String SESSION = "session";

    private AccountData accountData;

    public boolean isLan = false;
    private String TGTToken;   //统一登录TGT令牌
    private String VPNSession; //VPN认证Session
    private String session;    //教务系统认证Session

    public String getCookie() {
        return null;
    }

    private TokenData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        accountData = AccountData.newInstance(context);
        this.context = context;

        TGTToken = sharedPreferences.getString(TGT_TOKEN, null);
        VPNSession = sharedPreferences.getString(VPN_SESSION, null);
        session = sharedPreferences.getString(SESSION, null);
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
        StringBuilder cookie_builder = new StringBuilder();
        if (accountData.getIsLogin()) {
            int state = StaticService.autoLogin(
                    context,
                    accountData.getUsername(),
                    accountData.getPassword(),
                    cookie_builder
            );
            if (state == 0) {
                //
                return 0;
            } else {
                return state;
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
    }

    public String getVPNSession() {
        return VPNSession;
    }

    public void setVPNSession(String VPNSession) {
        this.VPNSession = VPNSession;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}

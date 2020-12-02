package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

import top.yvyan.guettable.service.StaticService;

public class CookieData {
    private static CookieData cookieData;
    private SharedPreferences sharedPreferences;
    private Context context;

    private static final String SHP_NAME = "CookieData";
    private static String COOKIE = "cookie";

    private AccountData accountData;
    private String cookie;

    private CookieData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        accountData = AccountData.newInstance(context);
        this.context = context;
        load();
    }

    private void load() {
        cookie = sharedPreferences.getString(COOKIE, "");
    }

    public static CookieData newInstance(Context context) {
        if (cookieData == null) {
            cookieData = new CookieData(context);
        }
        return cookieData;
    }


    public String getCookie() {
        return cookie;
    }

    /**
     * state记录当前状态
     *  0 : 登录成功
     *  1 : 登录失效
     *  2 : 未登录
     *
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     *
     * 21 : 理论课更新成功
     * 22 : 课内实验更新成功
     * 23 : 考试安排更新成功
     *
     */

    /**
     * 刷新登录凭证
     * @return state记录当前状态
     *                 0 : 登录成功
     *                -1 : 密码错误
     *                -2 : 网络错误/未知错误
     *                -3 : 验证码连续错误
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
                setCookie(cookie_builder.toString());
                return 0;
            } else {
                return state;
            }
        } else {
            return 2;
        }
    }

    private void setCookie(String cookie) {
        this.cookie = cookie;
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COOKIE, cookie);
        editor.apply();
    }
}

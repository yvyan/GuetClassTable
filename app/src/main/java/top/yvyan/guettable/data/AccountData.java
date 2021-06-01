package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountData {
    private static AccountData accountData;
    private static final String SHP_NAME = "UserData";
    private static final String IS_SAVE = "isSave";
    private static final String IS_LOGIN = "isLogin";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String PASSWORD2 = "password2";
    SharedPreferences sharedPreferences;

    private boolean isSave;
    private boolean isLogin;
    private String username;
    private String bkjwPwd;
    private String VPNPwd;

    private AccountData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    public static AccountData newInstance(Context context) {
        if (accountData == null) {
            accountData = new AccountData(context);
        }
        return accountData;
    }

    public boolean getIsSave() {
        return isSave;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUser(String username, String bkjwPwd, String VPNPwd, boolean isSave) {
        if (bkjwPwd != null) {
            this.bkjwPwd = bkjwPwd;
        }
        this.username = username;
        this.VPNPwd = VPNPwd;
        this.isSave = isSave;
        this.isLogin = true;
        saveUser();
    }

    public void logoff() {
        this.isLogin = false;
        saveUser();
    }

    private void load() {
        isSave = sharedPreferences.getBoolean(IS_SAVE, false);
        isLogin = sharedPreferences.getBoolean(IS_LOGIN, false);
        username = sharedPreferences.getString(USERNAME, "");
        bkjwPwd = sharedPreferences.getString(PASSWORD, "");
        VPNPwd = sharedPreferences.getString(PASSWORD2, "");
    }

    private void saveUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAVE, isSave);
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, bkjwPwd);
        editor.putString(PASSWORD2, VPNPwd);
        editor.apply();
    }

    public String getBkjwPwd() {
        return bkjwPwd;
    }

    public String getVPNPwd() {
        return VPNPwd;
    }
}

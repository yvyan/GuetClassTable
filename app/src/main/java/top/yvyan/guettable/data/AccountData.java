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
    private String password;
    private String password2;

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

    public String getPassword() {
        return password;
    }

    public void setUser(String username, String password, boolean isSave) {
        this.username = username;
        this.password = password;
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
        password = sharedPreferences.getString(PASSWORD, "");
        password2 = sharedPreferences.getString(PASSWORD2, "");
    }

    private void saveUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAVE, isSave);
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD2, password2);
        editor.apply();
    }
}

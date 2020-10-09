package top.yvayn.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    private static UserData userData;
    private static final String SHP_NAME = "UserData";
    private static final String IS_SAVE = "isSave";
    private static final String IS_LOGIN = "isLogin";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    SharedPreferences sharedPreferences;

    private boolean isSave;
    private boolean isLogin;
    private String username;
    private String password;

    private UserData(Activity activity) {
        sharedPreferences = activity.getApplication().getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    public static UserData newInstance(Activity activity) {
        if (userData == null) {
            userData = new UserData(activity);
        }
        return userData;
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

    public void saveUser(String username, String password, boolean isSave) {
        this.username = username;
        this.password = password;
        this.isSave = isSave;
        this.isLogin = true;
        save();
    }

    public void delUser() {
        this.isLogin = false;
        this.isSave = false;
        this.username = "";
        this.password = "";
        clear();
    }

    public void logoff() {
        this.isLogin = false;
        save();
    }

    private void load() {
        isSave = sharedPreferences.getBoolean(IS_SAVE, false);
        isLogin = sharedPreferences.getBoolean(IS_LOGIN, false);
        username = sharedPreferences.getString(USERNAME, "");
        password = sharedPreferences.getString(PASSWORD, "");
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAVE, isSave);
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    private  void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}

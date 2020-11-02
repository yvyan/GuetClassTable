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
    private static final String IS_SAVE_COURSE = "isCourseSave";
    private static final String COURSE = "course";
    private static final String UPDATE_COURSE = "updateCourse";
    SharedPreferences sharedPreferences;

    private boolean isSave;
    private boolean isLogin;
    private String username;
    private String password;

    //临时存储测试
    private boolean isCourseSave;
    private String course;
    private boolean updateCourse;

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

    public void setUser(String username, String password, boolean isSave) {
        this.username = username;
        this.password = password;
        this.isSave = isSave;
        this.isLogin = true;
        saveUser();
    }

    public void delUser() {
        this.isLogin = false;
        this.isSave = false;
        this.username = "";
        this.password = "";
        clearUser();
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

        isCourseSave = sharedPreferences.getBoolean(IS_SAVE_COURSE, false);
        course = sharedPreferences.getString(COURSE, null);
        updateCourse = sharedPreferences.getBoolean(UPDATE_COURSE, false);
    }

    private void saveUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAVE, isSave);
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    private void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setUpdateCourse(boolean bool) {
        updateCourse = bool;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(UPDATE_COURSE, updateCourse);
        editor.apply();
    }

    public boolean getUpdateCourse() {
        return updateCourse;
    }

    public boolean isCourseSave() {
        return isCourseSave;
    }

    public String getCourse() {
        if (isCourseSave) {
            return course;
        }
        return null;
    }

    public void setCourse(String course) {
        this.course = course;
        updateCourse = true;
        isCourseSave = true;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COURSE, course);
        editor.putBoolean(IS_SAVE_COURSE, isCourseSave);
        editor.putBoolean(UPDATE_COURSE, true);
        editor.apply();
    }
}

package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import top.yvyan.guettable.util.TimeUtil;

public class GeneralData {
    private static GeneralData generalData;
    private static final String SHP_NAME = "GeneralData";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String WEEK = "week";
    private static final String TIME = "time";
    private static final String GRADE = "grade";
    private static final String TERM = "term";
    private static final String IS_INTERNATIONAL = "isInternational";
    private static final String LAST_UPDATE_TIME = "lastUpdateTime";
    private static final String APP_LAST_UPDATE_TIME = "appLastUpdateTime";
    private static final String RENEWABLE = "renewable";
    private static final String APPLY_PRIVACY = "applyPrivacy";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String name;
    private String number;
    private int week;
    private long time;
    private String grade;
    private String term;
    private boolean isInternational;
    private long lastUpdateTime;
    //控制检查更新频率
    private long appLastUpdateTime;
    private boolean renewable;
    //隐私协议
    private boolean applyPrivacy;

    private GeneralData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        load();
    }

    private void load() {
        name = sharedPreferences.getString(NAME, "");
        number = sharedPreferences.getString(NUMBER, "");
        week = sharedPreferences.getInt(WEEK, 1);
        time = sharedPreferences.getLong(TIME, System.currentTimeMillis());
        grade = sharedPreferences.getString(GRADE, null);
        term = sharedPreferences.getString(TERM, null);
        isInternational = sharedPreferences.getBoolean(IS_INTERNATIONAL, false);
        lastUpdateTime = sharedPreferences.getLong(LAST_UPDATE_TIME, -1);
        appLastUpdateTime = sharedPreferences.getLong(APP_LAST_UPDATE_TIME, -1);
        renewable = sharedPreferences.getBoolean(RENEWABLE, false);
        applyPrivacy = sharedPreferences.getBoolean(APPLY_PRIVACY, false);
    }

    public static GeneralData newInstance(Context context) {
        if (generalData == null) {
            generalData = new GeneralData(context);
        }
        return generalData;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        editor.putString(NUMBER, number);
        editor.apply();
    }

    public int getWeek() {
        int err = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
        if (week + err >= 20) {
            return 20;
        }
        return week + err;
    }

    public void setWeek(int week) {
        this.time = System.currentTimeMillis();
        this.week = week;
        editor.putLong(TIME, time);
        editor.putInt(WEEK, week);
        editor.apply();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        editor.putString(GRADE, grade);
        editor.apply();
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
        editor.putString(TERM, term);
        editor.apply();
    }

    public boolean isInternational() {
        return isInternational;
    }

    public void setInternational(boolean international) {
        isInternational = international;
        editor.putBoolean(IS_INTERNATIONAL, isInternational);
        editor.apply();
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE_TIME, lastUpdateTime);
        editor.apply();
    }

    public long getAppLastUpdateTime() {
        return appLastUpdateTime;
    }

    public void setAppLastUpdateTime(long appLastUpdateTime) {
        this.appLastUpdateTime = appLastUpdateTime;
        editor.putLong(APP_LAST_UPDATE_TIME, appLastUpdateTime);
        editor.apply();
    }

    public boolean isRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
        editor.putBoolean(RENEWABLE, renewable);
        editor.apply();
    }

    public boolean isApplyPrivacy() {
        return applyPrivacy;
    }

    public void setApplyPrivacy(boolean applyPrivacy) {
        this.applyPrivacy = applyPrivacy;
        editor.putBoolean(APPLY_PRIVACY, applyPrivacy);
    }
}

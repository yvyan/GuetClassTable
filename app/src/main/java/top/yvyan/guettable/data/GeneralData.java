package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import top.yvyan.guettable.util.TimeUtil;

public class GeneralData {
    private static GeneralData generalData;
    private static final String SHP_NAME = "GeneralData";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String MAX_WEEK = "maxWeek";
    private static final String WEEK = "week";
    private static final String TIME = "time";
    private static final String GRADE = "grade";
    private static final String TERM = "term";
    private static final String LAST_UPDATE_TIME = "lastUpdateTime";
    private static final String APPLY_PRIVACY = "applyPrivacy";
    private static final String WIDGET_THEME = "widget_theme";
    private static final String WIDGET_ALPHA = "widget_alpha";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String name;
    private String number;
    private int maxWeek;
    private int week;
    private long time;
    private String grade;
    private String term;
    private long lastUpdateTime;
    //隐私协议
    private boolean applyPrivacy;

    private String widget_theme;
    private int widget_alpha;

    @SuppressLint("CommitPrefEdits")
    private GeneralData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        load();
    }

    private void load() {
        name = sharedPreferences.getString(NAME, "");
        number = sharedPreferences.getString(NUMBER, "");
        maxWeek = sharedPreferences.getInt(MAX_WEEK, 20);
        week = sharedPreferences.getInt(WEEK, 1);
        time = sharedPreferences.getLong(TIME, System.currentTimeMillis());
        grade = sharedPreferences.getString(GRADE, null);
        term = sharedPreferences.getString(TERM, null);
        lastUpdateTime = sharedPreferences.getLong(LAST_UPDATE_TIME, -1);
        applyPrivacy = sharedPreferences.getBoolean(APPLY_PRIVACY, false);
        widget_theme = sharedPreferences.getString(WIDGET_THEME, "black");
        widget_alpha = sharedPreferences.getInt(WIDGET_ALPHA, 255);
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

    public int getMaxWeek() {
        return maxWeek;
    }

    public void setMaxWeek(int maxWeek) {
        this.maxWeek = maxWeek;
        editor.putInt(MAX_WEEK, maxWeek);
        editor.apply();
    }

    public int getWeek() {
        int err = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
        return Math.min(week + err, maxWeek);
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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE_TIME, lastUpdateTime);
        editor.apply();
    }

    public boolean isApplyPrivacy() {
        return applyPrivacy;
    }

    public void setApplyPrivacy(boolean applyPrivacy) {
        this.applyPrivacy = applyPrivacy;
        editor.putBoolean(APPLY_PRIVACY, applyPrivacy);
        editor.apply();
    }

    public String getWidget_theme() {
        return widget_theme;
    }

    public void setWidget_theme(String widget_theme) {
        this.widget_theme = widget_theme;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WIDGET_THEME, widget_theme);
        editor.apply();
    }

    public int getWidget_alpha() {
        return widget_alpha;
    }

    public void setWidget_alpha(int widget_alpha) {
        this.widget_alpha = widget_alpha;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WIDGET_ALPHA, widget_alpha);
        editor.apply();
    }
}

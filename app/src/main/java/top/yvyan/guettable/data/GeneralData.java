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
    private static final String LAST_UPDATE_TIME = "lastUpdateTime";
    SharedPreferences sharedPreferences;

    private String name;
    private String number;
    private int week;
    private long time;
    private String grade;
    private String term;
    private long lastUpdateTime;

    private GeneralData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        name = sharedPreferences.getString(NAME, "");
        number = sharedPreferences.getString(NUMBER, "");
        week = sharedPreferences.getInt(WEEK, 1);
        time = sharedPreferences.getLong(TIME, System.currentTimeMillis());
        grade = sharedPreferences.getString(GRADE, null);
        term = sharedPreferences.getString(TERM, null);
        lastUpdateTime = sharedPreferences.getLong(LAST_UPDATE_TIME, -1);
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
        saveName();
    }

    private void saveName() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        saveNumber();
    }

    private void saveNumber() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NUMBER, number);
        editor.apply();
    }

    public int getWeek() {
        int err = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
        week = week + err;
        if (week > 20) {
            week = 20;
        }
        return week;
    }

    public void setWeek(int week) {
        this.time = System.currentTimeMillis();
        this.week = week;
        saveWeek();
    }

    private void saveWeek() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME, time);
        editor.putInt(WEEK, week);
        editor.apply();
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
        saveGrade();
    }

    private void saveGrade() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GRADE, grade);
        editor.apply();
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
        saveTerm();
    }

    private void saveTerm() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TERM, term);
        editor.apply();
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        saveLastUpdateTime();
    }

    private void saveLastUpdateTime() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LAST_UPDATE_TIME, lastUpdateTime);
        editor.apply();
    }
}

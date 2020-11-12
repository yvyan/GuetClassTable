package top.yvyan.guettable.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import top.yvyan.guettable.util.TimeUtil;

public class GeneralData {
    private static GeneralData generalData;
    private static final String SHP_NAME = "GeneralData";
    private static final String WEEK = "week";
    private static final String TIME = "time";
    private static final String GRADE = "grade";
    private static final String TERM = "term";
    SharedPreferences sharedPreferences;

    private int week;
    private long time;
    private String grade;
    private String term;

    private GeneralData(Activity activity) {
        sharedPreferences = activity.getApplication().getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        week = sharedPreferences.getInt(WEEK, 1);
        time = sharedPreferences.getLong(TIME, System.currentTimeMillis());
        grade = sharedPreferences.getString(GRADE, null);
        term = sharedPreferences.getString(TERM, null);
    }

    public static GeneralData newInstance(Activity activity) {
        if (generalData == null) {
            generalData = new GeneralData(activity);
        }
        return generalData;
    }

    public int getWeek() {
        int err = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
        return week + err;
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
}
package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

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

    private static final String NEW_TERM = "new_term";
    private static final String SEMESTER_ID="SemesterId";

    private static final String ADD_TERM = "addTerm";
    private static final String LAST_UPDATE_TIME = "lastUpdateTime";
    private static final String APPLY_PRIVACY = "applyPrivacy";
    private static final String WIDGET_THEME = "widget_theme";
    private static final String WIDGET_ALPHA = "widget_alpha";

    private static final String AUTO_TERM_OPEN = "auto_term_open";
    private static final String AUTO_TERM_START_TIME = "auto_term_startTime";
    private static final String AUTO_TERM_END_TIME = "auto_term_endTime";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String name;
    private String number;
    private int maxWeek;
    private int week;
    private long time;
    private String grade;
    private String term;
    private String addTerm;

    private Integer semesterId;
    private String bkjwTestTerm;

    private long lastUpdateTime;
    //隐私协议
    private boolean applyPrivacy;

    private String widget_theme;
    private int widget_alpha;

    private static final MMKV mmkv = MMKV.defaultMMKV();

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
        semesterId = sharedPreferences.getInt(SEMESTER_ID, 65);
       // addTerm = sharedPreferences.getString(ADD_TERM, "");
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
        if (isAutoTerm()) {
            return getAutoWeek();
        } else {
            int offset = TimeUtil.calcWeekOffset(new Date(time), new Date(System.currentTimeMillis()));
            return Math.min(week + offset, maxWeek);
        }
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

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int SemesterId) {
        this.semesterId = SemesterId;
        editor.putInt(SEMESTER_ID, SemesterId);
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


    public String getAddTerm() {
        return addTerm;
    }

    public void setAddTerm(String addTerm) {
        this.addTerm = addTerm;
        editor.putString(ADD_TERM, addTerm);
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

    public static void setAutoTerm(boolean open) {
        mmkv.encode(AUTO_TERM_OPEN, open);
    }

    public static boolean isAutoTerm() {
        return mmkv.decodeBool(AUTO_TERM_OPEN, false);
    }

    public static void setStartTime(Long date) {
        mmkv.encode(AUTO_TERM_START_TIME, date);
    }

    public static void setEndTime(Long date) {
        mmkv.encode(AUTO_TERM_END_TIME, date);
    }

    public Date getStartTime() {
        if(isAutoTerm()) {
            long time = mmkv.getLong(AUTO_TERM_START_TIME, 0);
            return new Date(time);
        } else {
            return new Date(time - ((week-1) * 7L * 86400L * 1000L));
        }
    }

    public Date getEndTime() {
        if(isAutoTerm()) {
            long time = mmkv.getLong(AUTO_TERM_END_TIME, 0);
            if(time == 0) {
                return new Date(getStartTime().getTime()+((long)getMaxWeek()*86400L*7L*1000L));
            }
            return new Date(time);
        } else {
            return new Date((long)time+(long)getMaxWeek()*86400L*7L*1000L);
        }
    }


    /**
     * 自动获取星期数
     *
     * @return 星期数
     */
    private int getAutoWeek() {
        Date startTime = getStartTime();
        Date now = new Date();
        if (startTime.after(now)) {
            return 1;
        } else {
            return TimeUtil.calcWeekOffset(startTime, now) + 1;
        }
    }
}

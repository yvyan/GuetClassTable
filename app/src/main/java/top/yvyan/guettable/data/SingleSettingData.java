package top.yvyan.guettable.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 该类存储各个模块相互独立的设置信息
 */
public class SingleSettingData {
    private static SingleSettingData singleSettingData;
    private static final String SHP_NAME = "tableSettingData";
    private static final String HIDE_OTHER_WEEK = "hideOtherWeek";
    private static final String COMBINE_EXAM = "combineExam";
    private static final String HIDE_OUTDATED_EXAM = "hideOutdatedExam";
    SharedPreferences sharedPreferences;

    private boolean hideOtherWeek;     //(课程表)隐藏其它周的课程
    private boolean combineExam;       //(考试安排)合并考试安排
    private boolean hideOutdatedExam;  //(考试安排)隐藏过期的考试安排

    private SingleSettingData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        hideOtherWeek = sharedPreferences.getBoolean(HIDE_OTHER_WEEK, false);
        combineExam = sharedPreferences.getBoolean(COMBINE_EXAM, false);
        hideOutdatedExam = sharedPreferences.getBoolean(HIDE_OUTDATED_EXAM, false);
    }

    public static SingleSettingData newInstance(Context context) {
        if (singleSettingData == null) {
            singleSettingData = new SingleSettingData(context);
        }
        return singleSettingData;
    }

    //(课程表)隐藏其它周的课程
    public boolean isHideOtherWeek() {
        return hideOtherWeek;
    }

    public void setHideOtherWeek(boolean hideOtherWeek) {
        this.hideOtherWeek = hideOtherWeek;
        save();
    }

    //(考试安排)合并考试安排
    public boolean isCombineExam() {
        return combineExam;
    }

    public void setCombineExam(boolean combineExam) {
        this.combineExam = combineExam;
        save();
    }

    //(考试安排)隐藏过期的考试安排
    public boolean isHideOutdatedExam() {
        return hideOutdatedExam;
    }

    public void setHideOutdatedExam(boolean hideOutdatedExam) {
        this.hideOutdatedExam = hideOutdatedExam;
        save();
    }

    private void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HIDE_OTHER_WEEK, hideOtherWeek);
        editor.putBoolean(COMBINE_EXAM, combineExam);
        editor.putBoolean(HIDE_OUTDATED_EXAM, hideOutdatedExam);
        editor.apply();
    }
}

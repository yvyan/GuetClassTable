package top.yvyan.guettable.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 该类存储各个模块相互独立的设置信息
 */
public class SingleSettingData {
    private static SingleSettingData singleSettingData;
    private static final String SHP_NAME = "tableSettingData";
    private static final String HIDE_OTHER_WEEK = "hideOtherWeek";
    private static final String DATE_ALPHA = "dateAlpha";
    private static final String SLIDE_ALPHA = "slideAlpha";
    private static final String ITEM_ALPHA = "itemAlpha";
    private static final String TITLE_BAR_ALPHA = "titleBarAlpha";
    private static final String ITEM_LENGTH = "itemLength";
    private static final String COMBINE_EXAM = "combineExam";
    private static final String HIDE_OUTDATED_EXAM = "hideOutdatedExam";
    private static final String HIDE_OTHER_TERM_EXAM_SCORE = "hideOtherTermExamScore";
    private static final String HIDE_REPEAT_SCORE = "hideRepeatScore";
    private static final String THEME_ID = "theme_id";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // 课表页个性化
    private boolean hideOtherWeek;     //隐藏其它周的课程
    private float dateAlpha; //透明度
    private float slideAlpha;
    private float itemAlpha;
    private float titleBarAlpha;
    private int itemLength;
    private int themeId;

    private boolean combineExam;       //(考试安排)合并考试安排
    private boolean hideOutdatedExam;  //(考试安排)隐藏过期的考试安排

    private boolean hideOtherTermExamScore; //(考试成绩)隐藏其它学期的考试成绩

    private boolean hideRepeatScore; //(计划课程)隐藏必修课程中的限选和任选

    @SuppressLint("CommitPrefEdits")
    private SingleSettingData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        load();
    }

    private void load() {
        hideOtherWeek = sharedPreferences.getBoolean(HIDE_OTHER_WEEK, false);
        dateAlpha = sharedPreferences.getFloat(DATE_ALPHA, 0.6f);
        slideAlpha = sharedPreferences.getFloat(SLIDE_ALPHA, 0.0f);
        itemAlpha = sharedPreferences.getFloat(ITEM_ALPHA, 0.9f);
        titleBarAlpha = sharedPreferences.getFloat(TITLE_BAR_ALPHA, 127.5f);
        itemLength = sharedPreferences.getInt(ITEM_LENGTH, 60);

        themeId = sharedPreferences.getInt(THEME_ID,0);

        combineExam = sharedPreferences.getBoolean(COMBINE_EXAM, true);
        hideOutdatedExam = sharedPreferences.getBoolean(HIDE_OUTDATED_EXAM, true);
        hideOtherTermExamScore = sharedPreferences.getBoolean(HIDE_OTHER_TERM_EXAM_SCORE, false);
        hideRepeatScore = sharedPreferences.getBoolean(HIDE_REPEAT_SCORE, true);
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

    //(考试成绩)隐藏其它学期的考试成绩
    public boolean isHideOtherTermExamScore() {
        return hideOtherTermExamScore;
    }

    public void setHideOtherTermExamScore(boolean hideOtherTermExamScore) {
        this.hideOtherTermExamScore = hideOtherTermExamScore;
        save();
    }

    //(计划课程)隐藏必修课程中的限选和任选
    public boolean isHideRepeatScore() {
        return hideRepeatScore;
    }

    public void setHideRepeatScore(boolean hideRepeatScore) {
        this.hideRepeatScore = hideRepeatScore;
        save();
    }

    private void save() {
        editor.putBoolean(HIDE_OTHER_WEEK, hideOtherWeek);
        editor.putBoolean(COMBINE_EXAM, combineExam);
        editor.putBoolean(HIDE_OUTDATED_EXAM, hideOutdatedExam);
        editor.putBoolean(HIDE_OTHER_TERM_EXAM_SCORE, hideOtherTermExamScore);
        editor.putBoolean(HIDE_REPEAT_SCORE, hideRepeatScore);
        editor.apply();
    }

    public int getItemLength() {
        return itemLength;
    }

    public void setItemLength(int itemLength) {
        this.itemLength = itemLength;
        editor.putInt(ITEM_LENGTH, itemLength);
        editor.apply();
    }

    public float getDateAlpha() {
        return dateAlpha;
    }

    public float getSlideAlpha() {
        return slideAlpha;
    }

    public float getItemAlpha() {
        return itemAlpha;
    }

    public float getTitleBarAlpha() {
        return titleBarAlpha;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
        editor.putInt(THEME_ID, themeId);
        editor.apply();
    }

    public void setAlpha(float dateAlpha, float slideAlpha, float itemAlpha) {
        if (dateAlpha > 1) {
            dateAlpha = 1;
        } else if (dateAlpha < 0) {
            dateAlpha = 0;
        }
        if (slideAlpha > 1) {
            slideAlpha = 1;
        } else if (slideAlpha < 0) {
            slideAlpha = 0;
        }
        if (itemAlpha > 1) {
            itemAlpha = 1;
        } else if (itemAlpha < 0) {
            itemAlpha = 0;
        }
        this.dateAlpha = dateAlpha;
        this.slideAlpha = slideAlpha;
        this.itemAlpha = itemAlpha;

        editor.putFloat(DATE_ALPHA, dateAlpha);
        editor.putFloat(SLIDE_ALPHA, slideAlpha);
        editor.putFloat(ITEM_ALPHA, itemAlpha);
        editor.apply();
    }

    public void setAlpha(float dateAlpha, float slideAlpha, float itemAlpha, float titleBarAlpha) {
        if (dateAlpha > 1) {
            dateAlpha = 1;
        } else if (dateAlpha < 0) {
            dateAlpha = 0;
        }
        if (slideAlpha > 1) {
            slideAlpha = 1;
        } else if (slideAlpha < 0) {
            slideAlpha = 0;
        }
        if (itemAlpha > 1) {
            itemAlpha = 1;
        } else if (itemAlpha < 0) {
            itemAlpha = 0;
        }

        this.dateAlpha = dateAlpha;
        this.slideAlpha = slideAlpha;
        this.itemAlpha = itemAlpha;
        this.titleBarAlpha = titleBarAlpha;

        editor.putFloat(DATE_ALPHA, dateAlpha);
        editor.putFloat(SLIDE_ALPHA, slideAlpha);
        editor.putFloat(ITEM_ALPHA, itemAlpha);
        editor.putFloat(TITLE_BAR_ALPHA, titleBarAlpha);
        editor.apply();
    }
}

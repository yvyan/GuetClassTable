package top.yvyan.guettable.baseFun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import top.yvyan.guettable.activity.SetTermActivity;
import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.util.AppUtil;

/**
 * 用于版本变迁之后进行数据修改
 */
public class FirstLoad {
    private static final String SHP_NAME = "FirstLoadData";
    private static final String VERSION_CODE = "versionCode";

    private final Activity context;
    SharedPreferences.Editor editor;
    public int versionCode;
    public int nowVersionCode;

    @SuppressLint("CommitPrefEdits")
    public FirstLoad(Activity context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nowVersionCode =  AppUtil.getAppVersionCode(context);
        versionCode = sharedPreferences.getInt(VERSION_CODE, nowVersionCode);

    }

    public int check(int start) {
        for (int i = start; i < nowVersionCode; i++) {
            if(migrateData(i)) {
                return i;
            };
        }
        openUpdate();
        editor.putInt(VERSION_CODE, nowVersionCode);
        editor.apply();
        return nowVersionCode;
    }

    public int check() {
        if (nowVersionCode > versionCode) {
            return check(versionCode);
        }
        return nowVersionCode;
    }

    /**
     * 按版本号修改数据
     *
     * @param i 源版本号
     * @return Wait Activity
     */
    private boolean migrateData(int i) {
        switch (i) {
            case 40:
                //修复考试安排信息错误导致的闪退问题
                update_40();
                break;
            case 52:
                update_52();
                break;
            case 62:
                return update_62();
            default:
                break;
        }
        return false;
    }

    /**
     * 52->53需要进行的操作
     */
    private void update_52() {
        GeneralData.newInstance(context).setLastUpdateTime(-1);
    }

    private Boolean update_62() {
        if(GeneralData.isAutoTerm()) {
            Intent intent = new Intent(this.context, SetTermActivity.class);
            intent.putExtra("auto",true);
            this.context.startActivityForResult(intent,SetTermActivity.REQUEST_CODE);
            return true;
        }
        return false;
    }

    /**
     * 40->41需要进行的操作
     */
    private void update_40() {
        List<ExamBean> examBeans = ScheduleData.getExamBeans();
        for (ExamBean examBean : examBeans) {
            if (examBean.getClassNum() < 1) {
                examBean.setClassNum(1);
            }
        }
        ScheduleData.setExamBeans(examBeans);
    }

    private void openUpdate() {
        //打开检查更新
        SharedPreferences.Editor mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        mEditor.putBoolean(SettingActivity.SettingFragment.APP_CHECK_UPDATE, true);
        mEditor.apply();
    }
}

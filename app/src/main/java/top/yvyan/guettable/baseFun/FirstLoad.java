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
    int versionCode;

    @SuppressLint("CommitPrefEdits")
    public FirstLoad(Activity context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        versionCode = sharedPreferences.getInt(VERSION_CODE, 36);
    }

    public void check() {
        int nowVersionCode = AppUtil.getAppVersionCode(context);
        if (nowVersionCode > versionCode) {
            for (int i = versionCode; i < nowVersionCode; i++) {
                migrateData(i);
            }
            openUpdate();
            editor.putInt(VERSION_CODE, nowVersionCode);
            editor.apply();
        }
    }

    /**
     * 按版本号修改数据
     *
     * @param i 源版本号
     */
    private void migrateData(int i) {
        switch (i) {
            case 40:
                //修复考试安排信息错误导致的闪退问题
                update_40();
                break;
            case 52:
                update_52();
                break;
            case 62:
                update_62();
                break;
            default:
                break;
        }
    }

    /**
     * 52->53需要进行的操作
     */
    private void update_52() {
        GeneralData.newInstance(context).setLastUpdateTime(-1);
    }

    private void update_62() {
        if(GeneralData.isAutoTerm()) {
            Intent intent = new Intent(this.context, SetTermActivity.class);
            this.context.startActivityForResult(intent, SetTermActivity.REQUEST_CODE);
        }
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

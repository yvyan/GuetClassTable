package top.yvyan.guettable.baseFun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.util.AppUtil;

/**
 * 用于版本变迁之后进行数据修改
 */
public class FirstLoad {
    private static final String SHP_NAME = "FirstLoadData";
    private static final String VERSION_CODE = "versionCode";

    private final Context context;
    SharedPreferences.Editor editor;
    int versionCode;

    @SuppressLint("CommitPrefEdits")
    public FirstLoad(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        versionCode = sharedPreferences.getInt(VERSION_CODE, 36);
    }

    public void check() {
        int nowVersionCode = AppUtil.getAppVersionCode(context);
        if (nowVersionCode > versionCode) {
            for (int i = versionCode; i < nowVersionCode; i++) {
                updateDate(i);
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
    private void updateDate(int i) {
        switch (i) {
            case 36:
                update_36();
                break;
            case 40:
                //修复考试安排信息错误导致的闪退问题
                update_40();
                break;
            default:
                break;
        }
    }

    /**
     * 40->41需要进行的操作
     */
    private void update_40() {
        ScheduleData scheduleData = ScheduleData.newInstance(context);
        List<ExamBean> examBeans = scheduleData.getExamBeans();
        for (ExamBean examBean : examBeans) {
            if (examBean.getClassNum() < 1) {
                examBean.setClassNum(1);
            }
        }
        scheduleData.setExamBeans(examBeans);
    }

    /**
     * 36->37需要进行的操作
     */
    private void update_36() {
        //修复密码丢失导致的登录错误
        AccountData accountData = AccountData.newInstance(context);
        if (accountData.getIsLogin()) {
            if (accountData.getVPNPwd() == null || accountData.getVPNPwd().isEmpty()) {
                accountData.logoff();
            }
            if (accountData.getBkjwPwd() == null || accountData.getBkjwPwd().isEmpty()) {
                accountData.logoff();
            }
        }
    }

    private void openUpdate() {
        //打开检查更新
        SharedPreferences.Editor mEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        mEditor.putBoolean(SettingActivity.SettingFragment.APP_CHECK_UPDATE, true);
        mEditor.apply();
    }
}

package top.yvyan.guettable.baseFun;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SettingData;
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
        versionCode = sharedPreferences.getInt(VERSION_CODE, AppUtil.getAppVersionCode(context));
    }

    public void check() {
        int nowVersionCode = AppUtil.getAppVersionCode(context);
        if (nowVersionCode > versionCode) {
            for (int i = versionCode; i < nowVersionCode; i++) {
                updateDate(i);
            }
            editor.putInt(VERSION_CODE, nowVersionCode);
            editor.apply();
        }
    }

    /**
     * 按版本号修改数据
     * @param i 源版本号
     */
    private void updateDate(int i) {
        switch (i) {
            case 34:
                //调整密码存储
                update_34_35();
                break;
            case 24:
                update_24_25();
                break;
            default:
                break;
        }
    }

    /**
     * 34->35需要进行的操作
     */
    private void update_34_35() {
        //调整密码存储方式，修复了更换登录方式需要重新输入密码的问题
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenData", Context.MODE_PRIVATE);
        int loginType = sharedPreferences.getInt("loginType", 1);
        sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String password = sharedPreferences.getString("password", "");
        if (loginType == 0) { //CAS
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", "");
            editor.putString("password2", password);
            editor.apply();
        }
        //修复异常数据
        ScheduleData scheduleData = ScheduleData.newInstance(context);
        for (CourseBean courseBean : scheduleData.getLibBeans()) {
            if (courseBean.getDay() > 7) {
                courseBean.setDay(7);
            }
        }
        //打开检查更新
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(SettingActivity.SettingFragment.APP_CHECK_UPDATE, true);
        editor.apply();
    }

    /**
     * 24->25需要进行的操作
     */
    private void update_24_25() {
        // 修复之前版本的无年级数据导致的闪退问题
        if (GeneralData.newInstance(context).getGrade() == null) {
            AccountData.newInstance(context).logoff();
        }
        // 修复课表默认同步频率错误的问题
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingActivity.SettingFragment.REFRESH_DATA_FREQUENCY, "1");
        editor.apply();
    }
}

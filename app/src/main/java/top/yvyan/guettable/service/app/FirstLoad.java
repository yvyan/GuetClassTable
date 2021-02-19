package top.yvyan.guettable.service.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.AppUtil;

/**
 * 用于版本变迁之后进行数据修改
 */
public class FirstLoad {
    private static final String SHP_NAME = "FirstLoadData";
    private static final String VERSION_CODE = "versionCode";

    private final Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int versionCode;

    @SuppressLint("CommitPrefEdits")
    public FirstLoad(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        versionCode = sharedPreferences.getInt(VERSION_CODE, 23); //两个版本之后修改默认为当前版本号
    }

    public void check() {
        int nowVersionCode = AppUtil.getAppVersionCode(context);
        if (nowVersionCode > versionCode) {
            for (int i = nowVersionCode - 1; i >= versionCode; i--) {
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
            case 24:
                update_24_25();
                break;
            default:
                break;
        }
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

package top.yvyan.guettable;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.Keep;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyApp.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        initSophix();
    }

    private void initSophix() {
        String appVersion;
        Log.d(TAG, "init");
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = "1.0.0";
            e.printStackTrace();
        }

        // initialize最好放在attachBaseContext最前面
        String finalAppVersion = appVersion;
        Log.d(TAG, "init2");
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    // 补丁加载回调通知
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        // 表明补丁加载成功
                        Log.d(TAG, "补丁加载成功");
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        Log.d(TAG, "补丁生效需要重启");
                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后应用自杀
                    } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                        Log.d(TAG, "内部引擎异常");
                        // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                        // SophixManager.getInstance().cleanPatches();
                    } else {
                        Log.d(TAG, "其它错误信息");
                        Log.d(TAG, "+++" + info);
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
        Log.d(TAG, "init3");
    }
}

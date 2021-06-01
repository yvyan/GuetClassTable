package top.yvyan.guettable;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.Keep;

import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;

public class SophixStubApplication extends SophixApplication {

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
        String appVersion = "1.0.0";
        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // initialize最好放在attachBaseContext最前面
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}

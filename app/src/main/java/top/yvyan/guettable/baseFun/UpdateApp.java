package top.yvyan.guettable.baseFun;

import android.app.Activity;

import com.umeng.cconfig.UMRemoteConfig;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;

import top.yvyan.guettable.baseFun.service.OKHttpUpdateHttpService;
import top.yvyan.guettable.util.ToastUtil;

/**
 * APP检查更新
 */
public class UpdateApp {

    /**
     * XUpdate初始化
     *
     * @param activity activity
     * @param type     0 自动检查更新 1 手动检查更新
     */
    private static void initXUpdate(Activity activity, int type) {
        //设置版本更新出错的监听
        XUpdate.get()
                .debug(true)
                .isWifiOnly(false)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(activity))     //设置默认公共请求参数
                .param("appKey", activity.getPackageName())
                .setOnUpdateFailureListener(error -> {
                    if (type != 0) {
                        ToastUtil.showToast(activity, error.toString());
                    }
                })
                .supportSilentInstall(false)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(activity.getApplication());
    }

    private static void check(Activity activity, int type) {
        String url = UMRemoteConfig.getInstance().getConfigValue("XUpdateUrl");
        try {
            XUpdate.newBuild(activity)
                    .supportBackgroundUpdate(true)
                    .updateUrl(url)
                    .update();
        } catch (Exception ignored) {
            if (type != 0) {
                ToastUtil.showToast(activity, "更新功能异常，请稍后再试");
            }
        }
    }

    /**
     * 检查软件更新
     *
     * @param activity activity
     * @param type     0 自动检查更新 1 手动检查更新
     */
    public static void checkUpdate(Activity activity, int type) {
        initXUpdate(activity, type);
        check(activity, type);
    }
}

package top.yvyan.guettable.baseFun;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

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
     */
    public static void initXUpdate(Activity activity) {
        //设置版本更新出错的监听
        XUpdate.get()
                .debug(true)
                .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
                .isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(activity))     //设置默认公共请求参数
                .param("appKey", activity.getPackageName())
                .setOnUpdateFailureListener(error -> {
                    if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                        ToastUtil.showToast(activity, error.toString());
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(activity.getApplication());
    }

    public static void check(Activity activity) {
        String url = UMRemoteConfig.getInstance().getConfigValue("XUpdateUrl");
        XUpdate.newBuild(activity)
                .supportBackgroundUpdate(true)
                .updateUrl(url)
                .update();
    }
}

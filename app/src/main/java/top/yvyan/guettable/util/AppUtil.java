package top.yvyan.guettable.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.cconfig.UMRemoteConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtil {

    public static void Log(String text) {
        Log.d("15863", text + "\n");
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception ignored) {
        }
        return versioncode;
    }

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return appVersionName;
    }

    /**
     * 编码
     *
     * @param text 原字符串
     * @return 编码后的字符串
     * @Author telephone
     */
    public static String encode(String text) {
        StringBuilder sb = new StringBuilder();
        if (text != null) {
            char[] chs = text.toCharArray();
            for (char ch : chs) {
                if (ch < 128) {
                    sb.append(ch);
                } else {
                    sb.append('\\').append('u').append(String.format("%04x", (int) ch).toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    /**
     * 判断是否为WIFI
     *
     * @param context context
     * @return 是否为WIFI
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /****************
     *
     * 发起添加群流程。
     *
     * @param key 由官网生成的key
     ******************/
    public static void joinQQGroup(String key, Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    public static void addQQ(Context context) {
        String key = UMRemoteConfig.getInstance().getConfigValue("qunKey");
        joinQQGroup(key, context);
    }

    /**
     * 比较两个list内容是否相同
     *
     * @param list1 list
     * @param list2 list
     * @return equal
     */
    public static boolean equalList(List list1, List list2) {
        return (list1.size() == list2.size()) && list1.containsAll(list2);
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return Height
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static void reportFunc(Context context, String funcName) {
        Map<String, Object> funcMap = new HashMap<>();
        funcMap.put("name", funcName);
        MobclickAgent.onEventObject(context, "funcUsage", funcMap);
    }
}

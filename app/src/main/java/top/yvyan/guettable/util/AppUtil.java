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

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtil {

    public static void Log(String text) {
        Log.d("15863", text);
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
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
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
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

    /**
     * 编码
     * @Author     telephone
     * @param text 原字符串
     * @return     编码后的字符串
     */
    public static String encode(String text){
        StringBuilder sb = new StringBuilder();
        if (text != null){
            char[] chs = text.toCharArray();
            for (char ch : chs){
                if (ch < 128){
                    sb.append(ch);
                }else {
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
     * @return        是否为WIFI
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /****************
     *
     * 发起添加群流程。群号：桂电课程表交流群(963908505) 的 key 为： b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP
     * 调用 joinQQGroup(b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP) 即可发起手Q客户端申请加群 桂电课程表交流群(963908505)
     *
     * @param key 由官网生成的key
     ******************/
    private static void joinQQGroup(String key, Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
        }
    }

    public static void addQQ(Context context) {
        String key = "b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP";
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

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    @ColorInt
    public static int setAlphaComponent(@ColorInt int color,
                                        @IntRange(from = 0x0, to = 0xFF) int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (color & 0x00ffffff) | (alpha << 24);
    }
}

package top.yvyan.guettable.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class AppUtil {
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
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    private static boolean joinQQGroup(String key, Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void addQQ(Context context) {
        String key = "b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP";
        joinQQGroup(key, context);
    }
}

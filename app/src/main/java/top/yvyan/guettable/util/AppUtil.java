package top.yvyan.guettable.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
}

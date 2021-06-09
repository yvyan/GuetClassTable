package top.yvyan.guettable.widget;

import android.content.Context;
import android.content.Intent;

// 小部件工具类
public class WidgetUtil {

    /**
     * 调用小部件刷新的外部方法
     *
     * @param context context
     */
    public static void notifyWidgetUpdate(Context context) {
        Intent widgetIntent = new Intent(GuetTableAppWidget.USER_ACTION);
        widgetIntent.setPackage(context.getPackageName());
        context.sendBroadcast(widgetIntent);
    }




}

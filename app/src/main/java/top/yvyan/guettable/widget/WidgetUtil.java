package top.yvyan.guettable.widget;

import android.content.Context;
import android.content.Intent;

// 小部件工具类
public class WidgetUtil {

    /**
     * 调用小部件刷新外部方法
     *
     * @param context context
     */
    public static void notifyWidgetUpdate(Context context) {
        Intent widgetIntent = new Intent(GuetTableAppWidget.USER_ACTION);
        widgetIntent.setPackage(context.getPackageName());
        context.sendBroadcast(widgetIntent);
    }


    /**
     * 调用小部件换色外部方法
     *
     * @param context context
     * @param color   颜色
     */
    public static void notifyWidgetUpdateColor(Context context, String color) {
        Intent widgetIntent = new Intent(GuetTableAppWidget.USER_ACTION_COLOR);
        widgetIntent.setPackage(context.getPackageName());
        widgetIntent.putExtra("color", color);
        context.sendBroadcast(widgetIntent);
    }

    /**
     * 调用小部件更换透明度方法
     *
     * @param context context
     * @param alpha   透明度
     */
    public static void notifyWidgetUpdateAlpha(Context context, int alpha) {
        Intent widgetIntent = new Intent(GuetTableAppWidget.USER_ACTION_ALPHA);
        widgetIntent.setPackage(context.getPackageName());
        widgetIntent.putExtra("alpha", alpha);
        context.sendBroadcast(widgetIntent);
    }


}

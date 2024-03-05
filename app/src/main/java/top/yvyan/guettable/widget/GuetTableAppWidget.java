package top.yvyan.guettable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Objects;

import top.yvyan.guettable.MainActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

// 桌面部件
public class GuetTableAppWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "widget.refresh.action.CLICK";
    public static final String USER_ACTION = "widget.refresh.action.USER";
    public static final String USER_ACTION_ALPHA = "widget.refresh.action.ALPHA";
    public static final String USER_ACTION_COLOR = "widget.refresh.action.COLOR";
    public static final String TAG = "GuetTableAppWidget";
    private GeneralData generalData;

    //  更新全部界面信息
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        initData(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
        String color = generalData.getWidget_theme();
        switch (color) {
            case "black":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_black);
                break;
            case "red":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_red);
                break;
            case "pink":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_pink);
                break;
            case "blue":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_blue);
                break;
            case "orange":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_orange);
                break;
            case "green":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_green);
                break;
        }
        rv.setInt(R.id.img_cover, "setImageAlpha", generalData.getWidget_alpha());
        updateWidgetInfo(context, appWidgetManager, appWidgetId, rv);
    }

    //  更新部件透明度
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, int alpha) {
        initData(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
        rv.setInt(R.id.img_cover, "setImageAlpha", alpha);
        updateWidgetInfo(context, appWidgetManager, appWidgetId, rv);
    }

    //  更新部件颜色
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, String color) {
        initData(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
        switch (color) {
            case "black":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_black);
                break;
            case "red":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_red);
                break;
            case "pink":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_pink);
                break;
            case "blue":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_blue);
                break;
            case "orange":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_orange);
                break;
            case "green":
                rv.setImageViewResource(R.id.img_cover, R.drawable.shape_img_green);
                break;
        }
        rv.setInt(R.id.img_cover, "setImageAlpha", generalData.getWidget_alpha());
        updateWidgetInfo(context, appWidgetManager, appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AppUtil.reportFunc(context, "添加日课表微件");
        initData(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AppUtil.reportFunc(context, "移除日课表微件");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "action: " + intent.getAction());
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, GuetTableAppWidget.class));
        if (Objects.equals(intent.getAction(), CLICK_ACTION)) {     // 小部件刷新按钮事件
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId);
            }
            ToastUtil.showToast(context, "刷新成功");
        } else if (Objects.equals(intent.getAction(), USER_ACTION)) {   // 刷新按钮之外的刷新请求
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId);
            }
        } else if (Objects.equals(intent.getAction(), USER_ACTION_COLOR)) {   // 改变颜色
            String color = intent.getStringExtra("color");
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId, color);
            }
        } else if (Objects.equals(intent.getAction(), USER_ACTION_ALPHA)) {    // 改变透明度
            int alpha = intent.getIntExtra("alpha", 255);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId, alpha);
            }
        }
    }

    // 初始化数据
    private void initData(Context context) {
        if (generalData == null) {
            generalData = GeneralData.newInstance(context);
        }
    }

    private void updateWidgetInfo(Context context, AppWidgetManager appWidgetManager,
                                  int appWidgetId, RemoteViews rv) {
        try {
            Intent intent = new Intent(CLICK_ACTION);
            intent.putExtra("appWidgetId", appWidgetId);
            intent.setPackage(context.getPackageName());
            intent.setComponent(new ComponentName(context, GuetTableAppWidget.class));
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
            rv.setOnClickPendingIntent(R.id.widget_btn_refresh, pending);

            // 各组件的点击事件
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.setPackage(context.getPackageName());
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, activityIntent, PendingIntent.FLAG_MUTABLE);
            PendingIntent pendingIntentTemplate = PendingIntent.getActivity(
                    context, 0, activityIntent, PendingIntent.FLAG_MUTABLE);
            rv.setOnClickPendingIntent(R.id.rl_empty, pendingIntent);
            rv.setOnClickPendingIntent(R.id.rl_widget, pendingIntent);
            rv.setPendingIntentTemplate(R.id.widget_lv_class, pendingIntentTemplate);

            //  设置日期
            String day = TimeUtil.whichDay(TimeUtil.getDay() + 1);
            rv.setTextViewText(R.id.widget_week, "第".concat(String.valueOf(generalData.getWeek())).concat("周\t").concat(day));

            // 设置课程列表信息
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.widget_lv_class, serviceIntent);
            rv.setEmptyView(R.id.widget_lv_class, R.id.rl_empty);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_lv_class);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
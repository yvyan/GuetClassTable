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

import com.xuexiang.xui.widget.toast.XToast;

import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.TimeUtil;

// 桌面部件
public class GuetTableAppWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "widget.refresh.action.CLICK";
    public static final String TAG = "GuetTableAppWidget";
    private GeneralData generalData;

    //  更新部件界面信息
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        initData(context);
        Log.i(TAG, "update with appWidgetId.");
        try {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
            Intent intent = new Intent(CLICK_ACTION);
            intent.putExtra("appWidgetId", appWidgetId);
            intent.setComponent(new ComponentName(context, GuetTableAppWidget.class));
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
            rv.setOnClickPendingIntent(R.id.widget_btn_refresh, pending);

            //  设置日期
            String day = TimeUtil.whichDay(TimeUtil.getDay() + 1);
            rv.setTextViewText(R.id.widget_week, "第".concat(String.valueOf(generalData.getWeek())).concat("周\t").concat(day));

            // 设置课程列表信息
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.widget_lv_class, serviceIntent);
            rv.setEmptyView(R.id.widget_lv_class, R.layout.empty_view_widget_lv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_lv_class);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
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
        initData(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, intent.getAction());
        if (Objects.equals(intent.getAction(), CLICK_ACTION)) {     // 小部件刷新按钮事件
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, GuetTableAppWidget.class));
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, manager, appWidgetId);
            }
            XToast.success(context, "刷新成功").show();
        }
    }

    // 初始化数据
    private void initData(Context context) {
        if (generalData == null) {
            generalData = GeneralData.newInstance(context);
        }
    }


}
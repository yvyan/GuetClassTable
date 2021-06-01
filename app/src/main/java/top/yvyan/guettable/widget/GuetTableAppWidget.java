package top.yvyan.guettable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.xuexiang.xui.widget.toast.XToast;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;

public class GuetTableAppWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "top.yvyan.guettable.widget.action.CLICK";
    public static final String TAG = "GuetTableAppWidget";
    private GeneralData generalData;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
        Intent intent = new Intent(CLICK_ACTION);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.btn_refresh, pending);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        generalData = GeneralData.newInstance(context);
    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.getAction());
        if (intent.getAction().equals(CLICK_ACTION)) {
            XToast.success(context, "刷新成功").show();
        }
    }
}
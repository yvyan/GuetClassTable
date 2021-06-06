package top.yvyan.guettable.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.xuexiang.xui.widget.toast.XToast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.TimeUtil;

public class GuetTableAppWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "widget.refresh.action.CLICK";
    public static final String TAG = "GuetTableAppWidget";
    private GeneralData generalData;
    private Calendar calendar;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {
        if (generalData == null) {
            generalData = GeneralData.newInstance(context);
        }
        if (calendar == null) {
            calendar = Calendar.getInstance(Locale.getDefault());
        }
        try {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
            Intent intent = new Intent(CLICK_ACTION);
            intent.setComponent(new ComponentName(context, GuetTableAppWidget.class));
            PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);
            rv.setOnClickPendingIntent(R.id.widget_btn_refresh, pending);
            String term = generalData.getTerm();
            if (term != null) {
                rv.setTextViewText(R.id.widget_tv_term, context.getString(R.string.widget_tv_term).concat(term));
            }
            String day = TimeUtil.whichDay(TimeUtil.getDay());
            rv.setTextViewText(R.id.widget_tv_day, TimeUtil.timeFormat(calendar.getTime()));
            rv.setTextViewText(R.id.widget_week, "第".concat(String.valueOf(generalData.getWeek())).concat("周\t").concat(day));
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
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, intent.getAction());
        if (Objects.equals(intent.getAction(), CLICK_ACTION)) {
            ComponentName name = new ComponentName(context, GuetTableAppWidget.class);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.guet_table_app_widget);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(name, rv);
            XToast.success(context, "刷新成功").show();
        }
    }

}
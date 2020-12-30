package top.yvyan.guettable.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import top.yvyan.guettable.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {
    /**
     * 消息通知栏
     *
     * @param context 上下文
     * @param cl      需要跳转的Activity
     * @param tittle  通知栏标题
     * @param content 通知栏内容
     * @param i       通知的标识符
     */
    public static void showMessage(Context context, Class cl, String tittle, String content, int i) {
        Intent intent = new Intent(context, cl);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String id = context.getPackageName();//频道的ID。每个包必须是唯一的
        //渠道名字
        String name = context.getString(R.string.app_name);//频道的用户可见名称
        //创建一个通知管理器
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setContentTitle(tittle)//设置通知标题
                    .setContentText(content)//设置通知内容
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (context.getResources(), R.mipmap.ic_launcher))//设置大图标
                    .setContentIntent(pendingIntent)//打开消息跳转到这儿
                    .setAutoCancel(true)// 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                    .setOngoing(false)//将Ongoing设为true 那么notification将不能滑动删除
                    // 从Android4.1开始，可以通过以下方法，设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                    //.setPriority(NotificationCompat.PRIORITY_MAX)

                    // Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
                    // Notification.DEFAULT_SOUND：系统默认铃声。
                    // Notification.DEFAULT_VIBRATE：系统默认震动。
                    // Notification.DEFAULT_LIGHTS：系统默认闪光。
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(context)
                            .setContentTitle(tittle)
                            .setContentText(content)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource
                                    (context.getResources(), R.mipmap.ic_launcher))//设置大图标
                            .setContentIntent(pendingIntent)//打开消息跳转到这儿
                            .setAutoCancel(false)
                            .setOngoing(true)
                            //.setPriority(NotificationCompat.PRIORITY_MAX)
                            .setOngoing(true)
                            .setChannelId(id);//无效
            notification = notificationBuilder.build();
        }
        notificationManager.notify(i, notification);
    }

    /**
     * 删除消息通知栏
     *
     * @param context 上下文
     * @param i       通知的标识符
     */
    public static void destroy(Context context, int i) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(i);
    }
}

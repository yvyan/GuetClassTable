package top.yvyan.guettable.baseFun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.umcrash.UMCrash;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.yvyan.guettable.service.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.TimeUtil;

/**
 * 发布公告
 */
public class Notification {
    private static final String SHP_NAME = "NotificationData";
    private static final String ID = "id";
    private static final String LAST_SHOW_TIME = "lastShowTime";

    public static void getNotification(Activity activity) {
        try {
            String url = UMRemoteConfig.getInstance().getConfigValue("notificationUrl");
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try {
                        String result = Objects.requireNonNull(response.body()).string();
                        NotificationInfo notificationInfo = new Gson().fromJson(result, NotificationInfo.class);

                        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHP_NAME, Context.MODE_PRIVATE);
                        int id = sharedPreferences.getInt(ID, 0);
                        long lastShowTime = sharedPreferences.getLong(LAST_SHOW_TIME, -1);
                        if (notificationInfo.getMaxVersionCode() >= AppUtil.getAppVersionCode(activity)) {
                            if (id < notificationInfo.getId()) {
                                //展示弹窗
                                showNotification(activity, notificationInfo);
                            } else {
                                if (notificationInfo.getForce() == 2 && (lastShowTime == -1 || TimeUtil.calcDayOffset(new Date(lastShowTime), new Date()) >= 1)) {
                                    //展示弹窗
                                    showNotification(activity, notificationInfo);
                                } else if (notificationInfo.getForce() == 1) {
                                    //展示弹窗
                                    showNotification(activity, notificationInfo);
                                }
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(ID, notificationInfo.getId());
                            editor.putLong(LAST_SHOW_TIME, System.currentTimeMillis());
                            editor.apply();
                        }
                    } catch (Exception e) {
                        UMCrash.generateCustomLog(e, "getNotification");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showNotification(Activity activity, NotificationInfo info) {
        activity.runOnUiThread(() -> {
            if (info.type == 0) {
                DialogUtil.showNotificationDialog(activity, info.title, info.comm);
            } else {
                DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                    @Override
                    public void onClickYes() {
                        if (info.type == 1) { //APP内打开链接
                            CommFunc.openUrl(activity, null, info.context, true);
                        } else if (info.type == 3) { //打开对应activity
                            toActivity(activity, info.context);
                        } else if (info.type == 4) { //加群
                            AppUtil.joinQQGroup(info.context, activity);
                        } else {
                            CommFunc.openBrowser(activity, info.context);
                        }
                    }

                    @Override
                    public void onClickBack() {
                    }
                };
                String buttonText;
                if (info.button == null || info.button.isEmpty()) {
                    buttonText = "查看详情";
                } else {
                    buttonText = info.button;
                }
                DialogUtil.showDialog(activity, info.title, true, buttonText, "好的", info.comm, service);
            }
        });

    }

    /**
     * 跳转到对应activity
     */
    public static void toActivity(Context context, String fullName) {
        if (fullName != null && fullName.length() > 0) {
            try {
                Intent intent = new Intent(context, Class.forName(fullName));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class NotificationInfo {
        private int id;
        private int maxVersionCode;
        private int force;
        private String title;
        private String comm;
        private String button; //按钮名称
        // 0:无动作; 1:APP内打开链接; 2:使用浏览器打开链接; 3:打开对应activity; 4:添加QQ群
        private int type;
        private String context;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public int getMaxVersionCode() {
            return maxVersionCode;
        }

        public int getForce() {
            return force;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setComm(String comm) {
            this.comm = comm;
        }

        public String getComm() {
            return comm;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getButton() {
            return button;
        }

        public void setButton(String button) {
            this.button = button;
        }
    }

}

package top.yvyan.guettable.baseFun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.umeng.cconfig.UMRemoteConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

/**
 * APP检查更新
 */
public class UpdateApp {

    public static void check(Activity activity, int type) {
        //服务器更新
        try {
            if (type == 2) {
                ToastUtil.showToast(activity, "正在检查更新……");
            }
            GeneralData generalData = GeneralData.newInstance(activity);
            String url = UMRemoteConfig.getInstance().getConfigValue("updateUrl");
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    activity.runOnUiThread(() -> ToastUtil.showToast(activity, "更新服务器连接失败，请加群检查更新！"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try {
                        String result = Objects.requireNonNull(response.body()).string();
                        UpdateInfo updateInfo = new Gson().fromJson(result, UpdateInfo.class);
                        if (updateInfo.getVersioncode() > AppUtil.getAppVersionCode(activity)) {
                            if (type == 2) {
                                //显示弹窗
                                activity.runOnUiThread(() -> showUpdateDialog(activity, updateInfo.comm, updateInfo.getVersion(), updateInfo.url));
                            } else {
                                if (SettingData.newInstance(activity).isAppCheckUpdate() || updateInfo.getForce() <= 1) {
                                    if (updateInfo.getForce() <= 2 || generalData.getAppLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getAppLastUpdateTime()), new Date()) >= 1) {
                                        // 显示弹窗
                                        activity.runOnUiThread(() -> showUpdateDialog(activity, updateInfo.comm, updateInfo.getVersion(), updateInfo.url));
                                        // 刷新时间
                                        generalData.setAppLastUpdateTime(System.currentTimeMillis());
                                    }
                                }
                            }
                        } else {
                            if (type == 2) {
                                activity.runOnUiThread(() -> ToastUtil.showToast(activity, "已是最新版本！"));
                            }
                        }
                    } catch (Exception e) {
                        ToastUtil.showToast(activity, "更新服务器连接失败，请加群更新！");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示更新弹窗
     *
     * @param context 上下文
     * @param comm    更新说明
     * @param version 版本
     * @param url     下载链接
     */
    private static void showUpdateDialog(final Context context, String comm, String version, String url) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 创建对话框
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        window.setContentView(R.layout.update_dialog);
        TextView updateVersion = window.findViewById(R.id.tv_updateVersion);
        updateVersion.setText(version);
        TextView updateComm = window.findViewById(R.id.tv_updateComm);
        updateComm.setText(comm);
        Button btn_hint_yes = window.findViewById(R.id.btn_hint_yes);
        ImageView btn_hint_no = window.findViewById(R.id.imageView_no);
        Button btn_hint_addQQ = window.findViewById(R.id.btn_hint_addQQ);
        btn_hint_yes.setOnClickListener(arg0 -> {
            updateApp(context, url);
            dialog.dismiss();
        });
        btn_hint_no.setOnClickListener(arg0 -> dialog.dismiss());
        btn_hint_addQQ.setOnClickListener(arg0 -> {
            AppUtil.addQQ(context);
            dialog.dismiss();
        });
    }

    private static void updateApp(Context context, String url) {
        ToastUtil.showToast(context, "正在打开下载链接，请下载后安装");
        Uri uri = Uri.parse(url == null ? context.getResources().getString(R.string.downloadApp_url) : url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        context.startActivity(webIntent);
    }

    private static class UpdateInfo {
        private int versionCode;
        private String version;
        private int force;
        private String redText;
        private String comm;
        private String url;

        public int getVersioncode() {
            return versionCode;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        public int getForce() {
            return force;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}

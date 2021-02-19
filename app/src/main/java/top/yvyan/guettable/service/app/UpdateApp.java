package top.yvyan.guettable.service.app;

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

import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import java.util.Date;

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

    /**
     * 检查更新
     * @param type 1: 自动事件; 2: 用户点击;
     */
    public static void checkUpdate(Context context, int type) {
        GeneralData generalData = GeneralData.newInstance(context);
        XiaomiUpdateAgent.update(context);
        XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener((i, updateResponse) -> {
            switch (i) {
                case UpdateStatus.STATUS_UPDATE: //有更新
                    if (type == 2) {
                        //显示弹窗
                        showScanNumberDialog(context, updateResponse.updateLog, updateResponse.versionName);
                    } else {
                        if (SettingData.newInstance(context).isAppCheckUpdate()) {
                            if (generalData.getAppLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getAppLastUpdateTime()), new Date()) >= 1) {
                                // 显示弹窗
                                showScanNumberDialog(context, updateResponse.updateLog, updateResponse.versionName);
                                // 刷新时间
                                generalData.setAppLastUpdateTime(System.currentTimeMillis());
                            }
                        }
                    }
                    break;
                case UpdateStatus.STATUS_NO_UPDATE:
                    if (type == 2) {
                        ToastUtil.showToast(context, "已是最新版本！");
                    }
                    break;
                case UpdateStatus.STATUS_NO_NET:
                    if (type == 2) {
                        ToastUtil.showToast(context, "网络未连接！");
                    }
                    break;
                case UpdateStatus.STATUS_FAILED:
                    if (type == 2) {
                        ToastUtil.showToast(context, "服务器错误，请稍后重试！");
                    }
                    break;
                case UpdateStatus.STATUS_LOCAL_APP_FAILED:
                    if (type == 2) {
                        ToastUtil.showToast(context, "应用信息检查失败，请稍后重试！");
                    }
                default:
                    break;
            }
        });
    }

    /**
     * 显示更新弹窗
     *
     * @param context 上下文
     * @param text    自定义显示的文字
     * @param version 版本
     */
    private static void showScanNumberDialog(final Context context, String text, String version) {
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
        TextView tv_scan_number = window.findViewById(R.id.tv_updateComm);
        tv_scan_number.setText(text);
        Button btn_hint_yes = window.findViewById(R.id.btn_hint_yes);
        ImageView btn_hint_no = window.findViewById(R.id.imageView_no);
        Button btn_hint_addQQ = window.findViewById(R.id.btn_hint_addQQ);
        btn_hint_yes.setOnClickListener(arg0 -> {
            updateApp(context);
            dialog.dismiss();
        });
        btn_hint_no.setOnClickListener(arg0 -> dialog.dismiss());
        btn_hint_addQQ.setOnClickListener(arg0 -> {
            AppUtil.addQQ(context);
            dialog.dismiss();
        });
    }

    private static void updateApp(Context context) {
        ToastUtil.showToast(context, "正在打开下载链接，请下载后安装");
        Uri uri = Uri.parse(context.getResources().getString(R.string.downloadApp_url));
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        context.startActivity(webIntent);
    }
}

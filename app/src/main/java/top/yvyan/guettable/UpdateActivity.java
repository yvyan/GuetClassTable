package top.yvyan.guettable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.util.ToastUtil;

public class UpdateActivity extends AppCompatActivity {

    @BindView(R.id.update_loading)
    TextView updateLoading;
    @BindView(R.id.update_info)
    View updateInfo;
    @BindView(R.id.update_text)
    TextView updateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        XiaomiUpdateAgent.update(this);
        XiaomiUpdateAgent.setUpdateAutoPopup(false);

        XiaomiUpdateAgent.setUpdateListener((i, updateResponse) -> {
            switch (i) {
                case UpdateStatus.STATUS_UPDATE: //有更新
                    // 有更新， UpdateResponse为本次更新的详细信息
                    // 其中包含更新信息，下载地址，MD5校验信息等，可自行处理下载安装
                    // 如果希望 SDK继续接管下载安装事宜，可调用
                    updateLoading.setVisibility(View.GONE);
                    updateText.setText(updateResponse.updateLog);
                    updateInfo.setVisibility(View.VISIBLE);
                    //  XiaomiUpdateAgent.arrange()
                    break;
                case UpdateStatus.STATUS_NO_UPDATE:
                    updateInfo.setVisibility(View.GONE);
                    updateLoading.setText("已是最新版本！");
                    updateLoading.setVisibility(View.VISIBLE);
                    break;
                case UpdateStatus.STATUS_NO_NET: //无网络连接
                    updateInfo.setVisibility(View.GONE);
                    updateLoading.setText("网络未连接！");
                    updateLoading.setVisibility(View.VISIBLE);
                    break;
                case UpdateStatus.STATUS_FAILED: //服务器错误，请稍后重试
                    // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
                    updateInfo.setVisibility(View.GONE);
                    updateLoading.setText("服务器错误，请稍后重试！");
                    updateLoading.setVisibility(View.VISIBLE);
                    break;
                case UpdateStatus.STATUS_LOCAL_APP_FAILED: //检查更新失败
                    // 检查更新获取本地安装应用信息失败， UpdateResponse为null
                    updateInfo.setVisibility(View.GONE);
                    updateLoading.setText("应用信息检查失败，请前往QQ群重新！");
                    updateLoading.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        });
    }

    public void onClick(View view) {
        finish();
    }

    public void updateApp(View view) {
        if (!checkUnknownInstallPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ToastUtil.showToast(this, "请开启应用安装权限来安装应用！");
            toInstallPermissionSettingIntent();
        } else {
            ToastUtil.showToast(this, "正在更新，请稍后……");
            XiaomiUpdateAgent.arrange();
        }
    }

    /**
     * 检查是否有未知应用安装权限
     *
     * @return 检查结果
     */
    private boolean checkUnknownInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return this.getPackageManager().canRequestPackageInstalls();
        } else {
            return true;
        }
    }

    //开启安装未知来源权限
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:" + this.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        //系统将打开是未知来源应用的管理列表，需要用户手动设置未知来源应用安装权限
        startActivityForResult(intent, 1);
    }

    //权限开启后的回调函数
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_OK && requestCode == 1) {
            //权限开启成功，编写用户服务函数
            ToastUtil.showToast(this, "正在更新，请稍后……");
            XiaomiUpdateAgent.arrange();
        } else {
            ToastUtil.showToast(this, "未开启应用安装权限，无法安装更新！");
        }
    }

    public void updateAppCoolApk(View view) {
        Uri uri = Uri.parse(getApplicationContext().getResources().getString(R.string.downloadApp_url));
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }
}
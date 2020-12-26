package top.yvyan.guettable.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import java.util.Date;

import top.yvyan.guettable.AboutActivity;
import top.yvyan.guettable.HelperActivity;
import top.yvyan.guettable.LoginActivity;
import top.yvyan.guettable.MySettingActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.SetTermActivity;
import top.yvyan.guettable.ShareActivity;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonFragment extends Fragment implements View.OnClickListener {
    private static PersonFragment personFragment;

    private static final String TAG = "PersonFragment";
    private View view;

    private View person_userNameAndNo;
    private View person_login;
    private View person_setting;
    private Button buttonQuit;
    private View person_line_1;
    private View person_userInfo;
    private View person_userInfo_card;
    private TextView person_name;
    private TextView person_number;
    private TextView person_grade;
    private TextView person_term;
    private TextView person_week;
    private TextView profileVersion;

    //    private View info;
    private View help;
    private View share;
    private View update;
    private View download;
    private View about;

    private AccountData accountData;
    private GeneralData generalData;
    private OnButtonClick onButtonClick;

    private static AlertDialog dialog;

    public PersonFragment() {
    }

    public static PersonFragment newInstance() {
        if (personFragment == null) {
            personFragment = new PersonFragment();
        }
        return personFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "createPersonFragmentView");
        view = inflater.inflate(R.layout.fragement_preson, container, false);
        accountData = AccountData.newInstance(getContext());
        generalData = GeneralData.newInstance(getContext());
        initView();
        updateView();
        person_setting = view.findViewById(R.id.person_setting);
        person_setting.setOnClickListener(this);
        // 检查更新
        checkUpdate(1);
        return view;
    }

    private void initView() {
        person_userNameAndNo = view.findViewById(R.id.person_userNameAndNo);
        person_login = view.findViewById(R.id.person_login);
        person_login.setOnClickListener(this);
        person_name = view.findViewById(R.id.person_name);
        person_number = view.findViewById(R.id.person_number);
        buttonQuit = view.findViewById(R.id.btn_quit);
        buttonQuit.setOnClickListener(this);
        person_line_1 = view.findViewById(R.id.person_line_1);
        person_userInfo = view.findViewById(R.id.person_userInfo);
        person_userInfo_card = view.findViewById(R.id.person_userInfo_card);
        person_userInfo_card.setOnClickListener(this);
        person_grade = view.findViewById(R.id.person_grade);
        person_term = view.findViewById(R.id.person_term);
        person_week = view.findViewById(R.id.person_week);
        profileVersion = view.findViewById(R.id.tv_profile_version);
        profileVersion.setText(AppUtil.getAppVersionName(getContext()));

        help = view.findViewById(R.id.person_help);
        help.setOnClickListener(this);
        share = view.findViewById(R.id.person_share);
        share.setOnClickListener(this);
        update = view.findViewById(R.id.person_update);
        update.setOnClickListener(this);
        download = view.findViewById(R.id.person_download);
        download.setOnClickListener(this);
        if (generalData.isRenewable()) {
            download.setVisibility(View.VISIBLE);
        } else {
            download.setVisibility(View.GONE);
        }
        about = view.findViewById(R.id.person_about);
        about.setOnClickListener(this);
    }

    public void updateView() {
        if (accountData.getIsLogin()) {
            person_userNameAndNo.setVisibility(View.VISIBLE);
            person_line_1.setVisibility(View.VISIBLE);
            person_userInfo.setVisibility(View.VISIBLE);
            buttonQuit.setVisibility(View.VISIBLE);
            person_login.setVisibility(View.GONE);
            person_name.setText(generalData.getName());
            person_number.setText(generalData.getNumber());
            person_grade.setText(generalData.getGrade() + "级");
            person_term.setText(generalData.getTerm());
            person_week.setText("第" + generalData.getWeek() + "周");

        } else {
            person_userNameAndNo.setVisibility(View.GONE);
            person_line_1.setVisibility(View.GONE);
            person_userInfo.setVisibility(View.GONE);
            buttonQuit.setVisibility(View.GONE);
            person_login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.person_userInfo_card:
            case R.id.person_login:
                if (accountData.getIsLogin()) {
                    intent = new Intent(getContext(), SetTermActivity.class);
                } else {
                    intent = new Intent(getContext(), LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.btn_quit:
                accountData.logoff();
                updateView();
                break;
            case R.id.person_setting:
                intent = new Intent(getContext(), MySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.person_help:
                intent = new Intent(getContext(), HelperActivity.class);
                startActivity(intent);
                break;
            case R.id.person_share:
                intent = new Intent(getContext(), ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.person_update:
                ToastUtil.showToast(getContext(), "正在检查更新……");
                checkUpdate(2);
                break;
            case R.id.person_download:
                updateAppCoolApk();
                break;
            case R.id.person_about:
                intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
            default:
                ToastUtil.showToast(getContext(), "尽请期待");
        }
    }

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    /**
     * 检查更新
     * @param type 1: 自动事件; 2: 用户点击;
     */
    private void checkUpdate(int type) {
        XiaomiUpdateAgent.update(getContext());
        XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener((i, updateResponse) -> {
            switch (i) {
                case UpdateStatus.STATUS_UPDATE: //有更新
                    if (type == 2) {
                        //显示弹窗
                        showScanNumberDialog(getContext(), updateResponse.updateLog, R.drawable.d_shengji);
                    } else {
                        generalData.setRenewable(true);
                        download.setVisibility(View.VISIBLE);
                        if (SettingData.newInstance(getContext()).isAppCheckUpdate()) {
                            if (generalData.getAppLastUpdateTime() == -1 || TimeUtil.calcDayOffset(new Date(generalData.getAppLastUpdateTime()), new Date()) >= 3) {
                                // 显示弹窗
                                showScanNumberDialog(getContext(), updateResponse.updateLog, R.drawable.d_shengji);
                                // 刷新时间
                                generalData.setAppLastUpdateTime(System.currentTimeMillis());
                            }
                        }
                    }
                    break;
                case UpdateStatus.STATUS_NO_UPDATE:
                    if (type == 2) {
                        ToastUtil.showToast(getContext(), "已是最新版本！");
                    } else {
                        generalData.setRenewable(false);
                        download.setVisibility(View.GONE);
                    }
                    break;
                case UpdateStatus.STATUS_NO_NET:
                    if (type == 2) {
                        ToastUtil.showToast(getContext(), "网络未连接！");
                    }
                    break;
                case UpdateStatus.STATUS_FAILED:
                    if (type == 2) {
                        ToastUtil.showToast(getContext(), "服务器错误，请稍后重试！");
                    }
                    break;
                case UpdateStatus.STATUS_LOCAL_APP_FAILED:
                    if (type == 2) {
                        ToastUtil.showToast(getContext(), "应用信息检查失败，请稍后重试！");
                    }
                default:
                    break;
            }
        });
    }

    /**
     * 显示弹窗
     *
     * @param context 上下文
     * @param text    自定义显示的文字
     * @param id      自定义图片资源
     */
    public void showScanNumberDialog(final Context context, String text, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 创建对话框
        dialog = builder.create();
        // 没有下面这句代码会导致自定义对话框还存在原有的背景

        // 弹出对话框
        dialog.show();
        // 以下两行代码是对话框的EditText点击后不能显示输入法的
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        Window window = dialog.getWindow();
        window.setContentView(R.layout.update_dialog);
        TextView tv_scan_number = (TextView) window
                .findViewById(R.id.tv_dialoghint);
        tv_scan_number.setText(text);
        // 实例化确定按钮
        Button btn_hint_yes = (Button) window.findViewById(R.id.btn_hint_yes);
        // 实例化取消按钮
        Button btn_hint_no = (Button) window.findViewById(R.id.btn_hint_no);
        // 实例化图片
        ImageView iv_dialoghint = (ImageView) window
                .findViewById(R.id.iv_dialoghint);
        // 自定义图片的资源
        iv_dialoghint.setImageResource(id);
        btn_hint_yes.setOnClickListener(arg0 -> {
            updateApp();
            dialog.dismiss();
        });
        btn_hint_no.setOnClickListener(arg0 -> {
            dialog.dismiss();
        });
    }

    public void updateApp() {
        if (!checkUnknownInstallPermission() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ToastUtil.showToast(getContext(), "请开启应用安装权限来安装应用！");
            toInstallPermissionSettingIntent();
        } else {
            ToastUtil.showToast(getContext(), "正在更新，请稍后……");
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
            return getContext().getPackageManager().canRequestPackageInstalls();
        } else {
            return true;
        }
    }

    //开启安装未知来源权限
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:" + getContext().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        //系统将打开是未知来源应用的管理列表，需要用户手动设置未知来源应用安装权限
        startActivityForResult(intent, 1);
    }

    //权限开启后的回调函数
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
            //权限开启成功，编写用户服务函数
            ToastUtil.showToast(getContext(), "正在更新，请稍后……");
            XiaomiUpdateAgent.arrange();
        } else {
            ToastUtil.showToast(getContext(), "未开启应用安装权限，无法安装更新！");
        }
    }

    public void updateAppCoolApk() {
        Uri uri = Uri.parse(getContext().getResources().getString(R.string.downloadApp_url));
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }
}

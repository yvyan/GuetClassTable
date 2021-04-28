package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.umeng.cconfig.UMRemoteConfig;

import java.util.Objects;

import top.yvyan.guettable.MainActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.activity.AboutActivity;
import top.yvyan.guettable.activity.LoginActivity;
import top.yvyan.guettable.activity.PersonalizedActivity;
import top.yvyan.guettable.activity.SetTermActivity;
import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.app.UpdateApp;
import top.yvyan.guettable.service.table.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonFragment extends Fragment implements View.OnClickListener {

    private View view;

    private View person_userNameAndNo;
    private View person_login;
    private Button buttonQuit;
    private View person_line_1;
    private View person_userInfo;
    private TextView person_name;
    private TextView person_number;
    private TextView person_grade;
    private TextView person_term;
    private TextView person_week;

    private AccountData accountData;
    private GeneralData generalData;

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_person, container, false);
        initData();
        initView();
        updateView();
        View person_setting = view.findViewById(R.id.person_setting);
        person_setting.setOnClickListener(this);
        return view;
    }

    private void initData() {
        accountData = AccountData.newInstance(getContext());
        generalData = GeneralData.newInstance(getContext());
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        View addStatus = view.findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getContext()));
        addStatus.setLayoutParams(lp);

        person_userNameAndNo = view.findViewById(R.id.person_userNameAndNo);
        person_login = view.findViewById(R.id.person_login);
        person_login.setOnClickListener(this);
        person_name = view.findViewById(R.id.person_name);
        person_number = view.findViewById(R.id.person_number);
        buttonQuit = view.findViewById(R.id.btn_quit);
        buttonQuit.setOnClickListener(this);
        person_line_1 = view.findViewById(R.id.person_line_1);
        person_userInfo = view.findViewById(R.id.person_userInfo);
        View person_userInfo_card = view.findViewById(R.id.person_userInfo_card);
        person_userInfo_card.setOnClickListener(this);
        person_grade = view.findViewById(R.id.person_grade);
        person_term = view.findViewById(R.id.person_term);
        person_week = view.findViewById(R.id.person_week);
        TextView profileVersion = view.findViewById(R.id.tv_profile_version);
        profileVersion.setText(AppUtil.getAppVersionName(Objects.requireNonNull(getContext())) + "(" + 0 + ")");

        //    private View info;
        View personPersonalized = view.findViewById(R.id.person_personalized);
        personPersonalized.setOnClickListener(this);
        View share = view.findViewById(R.id.person_share);
        share.setOnClickListener(this);
        View update = view.findViewById(R.id.person_update);
        update.setOnClickListener(this);
        View downloadAll = view.findViewById(R.id.person_download_all);
        downloadAll.setOnClickListener(this);
        View about = view.findViewById(R.id.person_about);
        about.setOnClickListener(this);
        View helpMe = view.findViewById(R.id.person_help_me);
        helpMe.setOnClickListener(this);
    }

    /**
     * 设置个人信息
     */
    @SuppressLint("SetTextI18n")
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

    /**
     * 设置背景
     *
     * @param setBackground 是否设置了背景
     */
    private void setBackground(boolean setBackground) {
        View addStatus = view.findViewById(R.id.add_status);
        View titleBar = view.findViewById(R.id.title_bar);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(getContext());
        if (setBackground) {
            titleBar.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            addStatus.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
        } else {
            titleBar.getBackground().setAlpha(255);
            addStatus.getBackground().setAlpha(255);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setBackground(BackgroundUtil.isSetBackground(getContext()));
        initData();
    }

    /**
     * 点击处理事件
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.person_userInfo_card:
            case R.id.person_login:
                if (accountData.getIsLogin()) {
                    AppUtil.reportFunc(getContext(), "设置学期");
                    intent = new Intent(getContext(), SetTermActivity.class);
                    startActivityForResult(intent, SetTermActivity.REQUEST_CODE);
                } else {
                    AppUtil.reportFunc(getContext(), "登录");
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, LoginActivity.REQUEST_CODE);
                }
                break;
            case R.id.btn_quit:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.btn_quit));
                accountData.logoff();
                updateView();
                break;
            case R.id.person_setting:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_setting));
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.person_personalized:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_personalized));
                intent = new Intent(getContext(), PersonalizedActivity.class);
                startActivity(intent);
                break;
            case R.id.person_share:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_share));
                shareText();
                break;
            case R.id.person_update:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_update));
                ToastUtil.showToast(getContext(), "正在检查更新……");
                UpdateApp.check(getActivity(), 2);
                break;
            case R.id.person_download_all:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_download_all));
                downloadAllApk();
                break;
            case R.id.person_about:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_about));
                intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.person_help_me:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_help_me));
                DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                    @Override
                    public void onClickYes() {
                        try {
                            ToastUtil.showToast(getContext(), "感谢支持！");
                            startActivity(Intent.parseUri(getResources().getString(R.string.alipay_url), Intent.URI_INTENT_SCHEME));
                        } catch (Exception e) {
                            ToastUtil.showToast(getContext(), "打开支付宝失败");
                        }
                    }

                    @Override
                    public void onClickBack() {
                    }
                };
                DialogUtil.showDialog(getContext(), "感谢您", false, "捐赠", "取消", getString(R.string.thanks_help), service);
                break;
            default:
                ToastUtil.showToast(getContext(), "尽请期待");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SetTermActivity.OK || resultCode == LoginActivity.OK) { //若登录成功或者切换时间则刷新个人信息并且切换到第一页进行课程信息同步
            updateView();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.onClick(0); //切换页面0
            }
        } else if (resultCode == SetTermActivity.OFF) {
            updateView();
        }
    }

    /**
     * 下载所有版本的安装包(nextCloud)
     */
    public void downloadAllApk() {
        AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_download_all));
        String url = UMRemoteConfig.getInstance().getConfigValue("cloudUrl");
        if (url == null || url.isEmpty()) {
            DialogUtil.showTextDialog(getContext(), "功能维护中！");
            return;
        }
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    /**
     * 分享给同学
     */
    public void shareText() {
        CommFunc.shareText(Objects.requireNonNull(getActivity()), "分享给同学", UMRemoteConfig.getInstance().getConfigValue("shareText"));
    }
}

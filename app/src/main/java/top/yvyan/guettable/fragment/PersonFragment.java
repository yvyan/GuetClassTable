package top.yvyan.guettable.fragment;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import top.yvyan.guettable.activity.AboutActivity;
import top.yvyan.guettable.activity.HelpTestActivity;
import top.yvyan.guettable.activity.LoginActivity;
import top.yvyan.guettable.activity.PersonalizedActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.activity.SetTermActivity;
import top.yvyan.guettable.activity.SettingActivity;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SettingData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.app.UpdateApp;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class PersonFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static PersonFragment personFragment;

    private static final String TAG = "PersonFragment";
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
    private OnButtonClick onButtonClick;

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
        personFragment = this;
        Log.d(TAG, "createPersonFragmentView");
        view = inflater.inflate(R.layout.fragement_preson, container, false);
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
        profileVersion.setText(AppUtil.getAppVersionName(Objects.requireNonNull(getContext())));

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
        View helpTest = view.findViewById(R.id.person_help_test);
        helpTest.setOnClickListener(this);
        View helpMe = view.findViewById(R.id.person_help_me);
        helpMe.setOnClickListener(this);
    }

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

    private void setBackground(boolean setBackground) {
        View addStatus = view.findViewById(R.id.add_status);
        View titleBar = view.findViewById(R.id.title_bar);
        if (setBackground) {
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
        } else {
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setBackground(BackgroundUtil.isSetBackground(getContext()));
        initData();
    }

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
                } else {
                    AppUtil.reportFunc(getContext(), "登录");
                    intent = new Intent(getContext(), LoginActivity.class);
                }
                startActivity(intent);
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
                UpdateApp.checkUpdate(getContext(), 2);
                break;
            case R.id.person_download_all:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_download_all));
                downloadAllApk();
                break;
            case R.id.person_help_test:
                AppUtil.reportFunc(getContext(), getResources().getString(R.string.person_help_test));
                helpTest();
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

    public OnButtonClick getOnButtonClick() {
        return onButtonClick;
    }

    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public void downloadAllApk() {
        Uri uri = Uri.parse(Objects.requireNonNull(getContext()).getResources().getString(R.string.downloadAll_url));
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        startActivity(webIntent);
    }

    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text));
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享给同学"));
    }

    public void helpTest() {
        if (SettingData.newInstance(getContext()).isDevelopMode()) {
            if (AppUtil.isWifi(Objects.requireNonNull(getContext()))) {
                DialogUtil.showScanNumberDialog(getContext(), "为了保证测试顺利，请关闭WIFI，连接数据网络后进行测试。");
            } else {
                Intent intent = new Intent(getContext(), HelpTestActivity.class);
                startActivity(intent);
            }
        } else {
            if (AppUtil.isWifi(Objects.requireNonNull(getContext()))) {
                DialogUtil.showScanNumberDialog(getContext(), "为了保证测试顺利，请关闭WIFI，连接数据网络后获取凭证。");
            } else {
                ToastUtil.showToast(getContext(), "请不要切换网络，正在获取凭证，请稍后！");
                new Thread(() -> {
                    TokenData tokenData = TokenData.newInstance(getContext());
                    int n = tokenData.refresh();
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        if (n == 0) {
                            //获取剪贴板管理器：
                            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            // 创建普通字符型ClipData
                            ClipData mClipData = ClipData.newPlainText("Label", tokenData.getCookie());
                            // 将ClipData内容放到系统剪贴板里。
                            cm.setPrimaryClip(mClipData);
                            DialogUtil.showScanNumberDialog(getContext(), "感谢协助，凭证复制成功，您现在可以发送给开发者了！");
                        } else {
                            DialogUtil.showScanNumberDialog(getContext(), "获取失败，请稍后重试。");
                        }
                    });
                }).start();
            }
        }
    }
}

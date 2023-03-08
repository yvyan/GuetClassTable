package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.umeng.cconfig.UMRemoteConfig;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.moreFun.CETActivity;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.ExperimentScoreActivity;
import top.yvyan.guettable.moreFun.GradesActivity;
import top.yvyan.guettable.moreFun.LibActivity;
import top.yvyan.guettable.moreFun.MoreUrlActivity;
import top.yvyan.guettable.moreFun.PlannedCoursesActivity;
import top.yvyan.guettable.moreFun.QQGroupActivity;
import top.yvyan.guettable.moreFun.ResitActivity;
import top.yvyan.guettable.moreFun.SelectedCourseActivity;
import top.yvyan.guettable.service.CommFunc;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class MoreFragment extends Fragment implements View.OnClickListener {

    private View view;

    private SingleSettingData singleSettingData;
    private GeneralData generalData;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_more, container, false);

        generalData = GeneralData.newInstance(getContext());
        singleSettingData = SingleSettingData.newInstance(getContext());
        //透明状态栏
        View addStatus = view.findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(requireContext());
        addStatus.setLayoutParams(lp);

        view.findViewById(R.id.more_test_schedule).setOnClickListener(this);
        view.findViewById(R.id.more_credits).setOnClickListener(this);
        view.findViewById(R.id.more_test_scores).setOnClickListener(this);
        view.findViewById(R.id.more_lib_scores).setOnClickListener(this);
        view.findViewById(R.id.more_resit_schedule).setOnClickListener(this);
        view.findViewById(R.id.more_lib_schedule).setOnClickListener(this);
        view.findViewById(R.id.more_selected_course).setOnClickListener(this);

        view.findViewById(R.id.more_plan_courses).setOnClickListener(this);
        view.findViewById(R.id.more_cet).setOnClickListener(this);

        view.findViewById(R.id.more_url_bkjw).setOnClickListener(this);
        view.findViewById(R.id.more_url_vpn).setOnClickListener(this);
        view.findViewById(R.id.more_url_campus).setOnClickListener(this);
        view.findViewById(R.id.more_url_lijiang).setOnClickListener(this);
        view.findViewById(R.id.more_url_graduation_project).setOnClickListener(this);
        view.findViewById(R.id.more_url_index).setOnClickListener(this);
        view.findViewById(R.id.more_url_more).setOnClickListener(this);

        view.findViewById(R.id.more_course_arrange).setOnClickListener(this);
        view.findViewById(R.id.more_empty_room).setOnClickListener(this);
        view.findViewById(R.id.more_qq_group).setOnClickListener(this);

        return view;
    }

    private void initData() {
        generalData = GeneralData.newInstance(getContext());
    }

    /**
     * 设置背景
     *
     * @param setBackground 是否设置背景
     */
    private void setBackground(boolean setBackground) {
        View addStatus = view.findViewById(R.id.add_status);
        View titleBar = view.findViewById(R.id.title_bar);
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
        setBackground(BackgroundUtil.isSetBackground(requireContext()));
        initData();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.more_test_schedule:
                intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.more_credits:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), GradesActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.more_test_scores:
                intent = new Intent(getContext(), ExamScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.more_lib_scores:
                intent = new Intent(getContext(), ExperimentScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.more_resit_schedule:
                intent = new Intent(getContext(), ResitActivity.class);
                startActivity(intent);
                break;
            case R.id.more_lib_schedule:
                intent = new Intent(getContext(), LibActivity.class);
                startActivity(intent);
                break;
            case R.id.more_selected_course:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), SelectedCourseActivity.class);
                    startActivity(intent);
                }
                break;


            case R.id.more_plan_courses:
                intent = new Intent(getContext(), PlannedCoursesActivity.class);
                startActivity(intent);
                break;
            case R.id.more_cet:
                intent = new Intent(getContext(), CETActivity.class);
                startActivity(intent);
                break;


            case R.id.more_url_bkjw:
                CommFunc.noLoginWebBKJW(getActivity());
                break;
            case R.id.more_url_vpn:
                CommFunc.noLoginWebVPN(getActivity());
                break;
            case R.id.more_url_campus:
                openBrowser(requireContext().getResources().getString(R.string.url_smart_campus));
                break;

            case R.id.more_url_lijiang:
                CommFunc.noLoginWebVPN(getActivity(),
                        requireContext().getResources().getString(R.string.url_lijiang),
                        requireContext().getResources().getString(R.string.url_lijiang_vpn));
                break;
            case R.id.more_url_graduation_project:
                openBrowser(requireContext().getResources().getString(R.string.url_graduation_project));
                break;
            case R.id.more_url_index:
                openBrowser(UMRemoteConfig.getInstance().getConfigValue("guetYvyanTop"));
                break;
            case R.id.more_url_more:
                intent = new Intent(getContext(), MoreUrlActivity.class);
                startActivity(intent);
                break;


            case R.id.more_course_arrange:
                CommFunc.noLoginWebVPN(getActivity(),
                        requireContext().getResources().getString(R.string.url_course_arrange),
                        requireContext().getResources().getString(R.string.url_course_arrange_vpn));
                break;
            case R.id.more_empty_room:
                CommFunc.noLoginWebVPN(getActivity(),
                        requireContext().getResources().getString(R.string.url_empty_room),
                        requireContext().getResources().getString(R.string.url_empty_room_vpn));
                break;
            case R.id.more_qq_group:
                intent = new Intent(getContext(), QQGroupActivity.class);
                startActivity(intent);
                break;


            default:
                ToastUtil.showToast(getContext(), "敬请期待！");
        }
    }

    /**
     * 打开链接
     *
     * @param url url
     */
    public void openBrowser(String url) {
        CommFunc.openUrl(getActivity(), null, url, true);
    }
}

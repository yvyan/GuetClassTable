package top.yvyan.guettable.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.umeng.cconfig.UMRemoteConfig;

import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.moreFun.AverageTeacherActivity;
import top.yvyan.guettable.moreFun.AverageTextbookActivity;
import top.yvyan.guettable.moreFun.CETActivity;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.ExperimentScoreActivity;
import top.yvyan.guettable.moreFun.GradesActivity;
import top.yvyan.guettable.moreFun.InnovationScoreActivity;
import top.yvyan.guettable.moreFun.LibActivity;
import top.yvyan.guettable.moreFun.MoreUrlActivity;
import top.yvyan.guettable.moreFun.PlannedCoursesActivity;
import top.yvyan.guettable.moreFun.QQGroupActivity;
import top.yvyan.guettable.moreFun.ResitActivity;
import top.yvyan.guettable.moreFun.SelectedCourseActivity;
import top.yvyan.guettable.moreFun.TestActivity;
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
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getContext()));
        addStatus.setLayoutParams(lp);

        View testSchedule = view.findViewById(R.id.more_test_schedule);
        testSchedule.setOnClickListener(this);
        View credits = view.findViewById(R.id.more_credits);
        credits.setOnClickListener(this);
        View testScores = view.findViewById(R.id.more_test_scores);
        testScores.setOnClickListener(this);
        View libScores = view.findViewById(R.id.more_lib_scores);
        libScores.setOnClickListener(this);
        View resitSchedule = view.findViewById(R.id.more_resit_schedule);
        resitSchedule.setOnClickListener(this);
        View libSchedule = view.findViewById(R.id.more_lib_schedule);
        libSchedule.setOnClickListener(this);

//        View graduationRequirement = view.findViewById(R.id.more_graduation_requirement);
//        graduationRequirement.setOnClickListener(this);
//        graduationRequirement.setVisibility(View.GONE);
        View planCourses = view.findViewById(R.id.more_plan_courses);
        planCourses.setOnClickListener(this);
        View cet = view.findViewById(R.id.more_cet);
        cet.setOnClickListener(this);
        View innovationPoints = view.findViewById(R.id.more_innovation_score);
        innovationPoints.setOnClickListener(this);

        View urlBkjw = view.findViewById(R.id.more_url_bkjw);
        urlBkjw.setOnClickListener(this);
        View urlVPN = view.findViewById(R.id.more_url_vpn);
        urlVPN.setOnClickListener(this);
        View urlCampus = view.findViewById(R.id.more_url_campus);
        urlCampus.setOnClickListener(this);
        View urlLiJiang = view.findViewById(R.id.more_url_lijiang);
        urlLiJiang.setOnClickListener(this);
        View urlMore = view.findViewById(R.id.more_url_more);
        urlMore.setOnClickListener(this);
        View urlIndex = view.findViewById(R.id.more_url_index);
        urlIndex.setOnClickListener(this);
        View qqGroup = view.findViewById(R.id.more_qq_group);
        qqGroup.setOnClickListener(this);

        View evaluatingTeachers = view.findViewById(R.id.more_evaluating_teachers);
        evaluatingTeachers.setOnClickListener(this);
        View evaluatingTextbooks = view.findViewById(R.id.more_evaluating_textbooks);
        evaluatingTextbooks.setOnClickListener(this);

        View selectedCourse = view.findViewById(R.id.more_selected_course);
        selectedCourse.setOnClickListener(this);

//        View test = view.findViewById(R.id.more_test);
//        test.setOnClickListener(this);
//        test.setVisibility(View.VISIBLE);

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
        setBackground(BackgroundUtil.isSetBackground(getContext()));
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
            case R.id.more_qq_group:
                intent = new Intent(getContext(), QQGroupActivity.class);
                startActivity(intent);
                break;

            case R.id.more_plan_courses:
                intent = new Intent(getContext(), PlannedCoursesActivity.class);
                startActivity(intent);
                break;
            case R.id.more_cet:
                intent = new Intent(getContext(), CETActivity.class);
                startActivity(intent);
                break;
            case R.id.more_innovation_score:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), InnovationScoreActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.more_url_bkjw:
                CommFunc.noLoginWebBKJW(getActivity());
                break;
            case R.id.more_url_vpn:
                openBrowser(Objects.requireNonNull(getContext()).getResources().getString(R.string.url_vpn));
                //CommFunc.noLoginWebVPN(getActivity());
                break;
            case R.id.more_url_campus:
                openBrowser(Objects.requireNonNull(getContext()).getResources().getString(R.string.smart_campus));
                break;

            case R.id.more_url_lijiang:
                openBrowser(Objects.requireNonNull(getContext()).getResources().getString(R.string.url_lijiang));
                break;
            case R.id.more_url_more:
                intent = new Intent(getContext(), MoreUrlActivity.class);
                startActivity(intent);
                break;
            case R.id.more_url_index:
                openBrowser(UMRemoteConfig.getInstance().getConfigValue("guetYvyanTop"));
                break;
            case R.id.more_evaluating_teachers:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), AverageTeacherActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.more_evaluating_textbooks:
                if (generalData.isInternational()) {
                    DialogUtil.showTextDialog(getContext(), "国际学院教务系统暂无此功能");
                } else {
                    intent = new Intent(getContext(), AverageTextbookActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.more_test:
                intent = new Intent(getContext(), TestActivity.class);
                startActivity(intent);
                break;
            case R.id.more_selected_course:
                intent = new Intent(getContext(), SelectedCourseActivity.class);
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

package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.moreFun.AverageTeacherActivity;
import top.yvyan.guettable.moreFun.CETActivity;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.ExperimentScoreActivity;
import top.yvyan.guettable.moreFun.GradesActivity;
import top.yvyan.guettable.moreFun.PlannedCoursesActivity;
import top.yvyan.guettable.util.ToastUtil;

public class MoreFragment extends Fragment implements View.OnClickListener {
    private static MoreFragment moreFragment;

    private View view;
    private View testSchedule, credits, testScores, libScores;
    private View graduationRequirement, planCourses, cet;
    private View urlBkjw, urlVPN, urlCampus, urlStaff, urlMore;
    private View evaluatingTeachers, evaluatingTextbooks;


    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_more, container, false);

        testSchedule = view.findViewById(R.id.more_test_schedule);
        testSchedule.setOnClickListener(this);
        credits = view.findViewById(R.id.more_credits);
        credits.setOnClickListener(this);
        testScores = view.findViewById(R.id.more_test_scores);
        testScores.setOnClickListener(this);
        libScores = view.findViewById(R.id.more_lib_scores);
        libScores.setOnClickListener(this);

        graduationRequirement = view.findViewById(R.id.more_graduation_requirement);
        graduationRequirement.setOnClickListener(this);
        planCourses = view.findViewById(R.id.more_plan_courses);
        planCourses.setOnClickListener(this);
        cet = view.findViewById(R.id.more_cet);
        cet.setOnClickListener(this);

        urlBkjw = view.findViewById(R.id.more_url_bkjw);
        urlBkjw.setOnClickListener(this);
        urlVPN = view.findViewById(R.id.more_url_vpn);
        urlVPN.setOnClickListener(this);
        urlCampus = view.findViewById(R.id.more_url_campus);
        urlCampus.setOnClickListener(this);
        urlStaff = view.findViewById(R.id.more_url_staff);
        urlStaff.setOnClickListener(this);
        urlMore = view.findViewById(R.id.more_url_more);
        urlMore.setOnClickListener(this);

        evaluatingTeachers = view.findViewById(R.id.more_evaluating_teachers);
        evaluatingTeachers.setOnClickListener(this);
        evaluatingTextbooks = view.findViewById(R.id.more_evaluating_textbooks);
        evaluatingTextbooks.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        Uri uri;
        switch (view.getId()) {
            case R.id.more_test_schedule:
                intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.more_credits:
                intent = new Intent(getContext(), GradesActivity.class);
                startActivity(intent);
                break;
            case R.id.more_test_scores:
                intent = new Intent(getContext(), ExamScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.more_lib_scores:
                intent = new Intent(getContext(), ExperimentScoreActivity.class);
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

            case R.id.more_url_bkjw:
                uri = Uri.parse(getContext().getResources().getString(R.string.url_bkjw));
                webIntent.setData(uri);
                startActivity(webIntent);
                break;
            case R.id.more_url_vpn:
                uri = Uri.parse(getContext().getResources().getString(R.string.url_vpn));
                webIntent.setData(uri);
                startActivity(webIntent);
                break;
            case R.id.more_url_campus:
                uri = Uri.parse(getContext().getResources().getString(R.string.smart_campus));
                webIntent.setData(uri);
                startActivity(webIntent);
                break;
            case R.id.more_url_staff:
                uri = Uri.parse(getContext().getResources().getString(R.string.url_xsgl));
                webIntent.setData(uri);
                startActivity(webIntent);
                break;
            case R.id.more_url_more:
                uri = Uri.parse(getContext().getResources().getString(R.string.guet_yvyan_top));
                webIntent.setData(uri);
                startActivity(webIntent);
                break;

            case R.id.more_evaluating_teachers:
                intent = new Intent(getContext(), AverageTeacherActivity.class);
                startActivity(intent);
                break;

            default:
                ToastUtil.showToast(getContext(), "敬请期待！");
        }
    }
}

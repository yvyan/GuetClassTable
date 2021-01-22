package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.EffectiveCredit;
import top.yvyan.guettable.Gson.PlannedCourse;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.PlannedCourseAdapter;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.AppUtil;

import static com.xuexiang.xui.XUI.getContext;

public class PlannedCoursesActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;
    private boolean update = false;

    @BindView(R.id.planned_course_state)
    TextView plannedCourseState;
    @BindView(R.id.planned_course_loading)
    View plannedCourseLoading;
    @BindView(R.id.planned_course_info_view)
    View infoView;
    @BindView(R.id.planned_course_info_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_courses);
        ButterKnife.bind(this);

        moreDate = MoreDate.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void updateView() {
        List<PlannedCourseBean> plannedCourseBeans = moreDate.getPlannedCourseBeans();
        if (plannedCourseBeans.size() == 0) {
            infoView.setVisibility(View.GONE);
            plannedCourseLoading.setVisibility(View.VISIBLE);
        } else {
            plannedCourseLoading.setVisibility(View.GONE);
            infoView.setVisibility(View.VISIBLE);
            PlannedCourseAdapter plannedCourseAdapter = new PlannedCourseAdapter(plannedCourseBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(plannedCourseAdapter);
        }
    }

    @Override
    public int updateData(String cookie) {
        List<PlannedCourseBean> plannedCourseBeans = StaticService.getPlannedCourseBeans(this, cookie);
        if (plannedCourseBeans != null) {
            if (!AppUtil.equalList(plannedCourseBeans, moreDate.getPlannedCourseBeans())) {
                moreDate.setPlannedCourseBeans(plannedCourseBeans);
                update = true;
            }
            return 5;
        } else {
            return 1;
        }
    }

    @Override
    public void updateView(String hint, int state) {
        plannedCourseState.setText(hint);
        if (state == 5 && update) {
            updateView();
        }
    }

    public void onClick(View view) {
        finish();
    }
}
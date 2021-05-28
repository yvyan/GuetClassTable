package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.PlannedCourseAdapter;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

import static com.xuexiang.xui.XUI.getContext;

public class PlannedCoursesActivity extends BaseFuncActivity {

    private MoreDate moreDate;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_plan_courses));
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_plan_courses));

        moreDate = MoreDate.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_planned_courses);
        RecyclerView recyclerView = findViewById(R.id.planned_course_info_recycler_view);
        List<PlannedCourseBean> plannedCourseBeans = moreDate.getPlannedCourseBeans();
        if (plannedCourseBeans.size() == 0) {
            showEmptyPage();
        } else {
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
}

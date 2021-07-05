package top.yvyan.guettable.moreFun;

import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.PlannedCourseAdapter;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

import static com.xuexiang.xui.XUI.getContext;

public class PlannedCoursesActivity extends BaseFuncActivity {

    private MoreDate moreDate;
    private SingleSettingData singleSettingData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_plan_courses));
//        setShowMore(true);
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_plan_courses));

        moreDate = MoreDate.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_planned_courses);
        RecyclerView recyclerView = findViewById(R.id.planned_course_info_recycler_view);
        List<PlannedCourseBean> plannedCourseBeans = moreDate.getPlannedCourseBeans();
//        if (singleSettingData.isHideRepeatScore()) {
//            plannedCourseBeans = CourseUtil.ridRepeatScore(plannedCourseBeans);
//        }
        if (plannedCourseBeans.size() == 0) {
            showEmptyPage();
        } else {
            PlannedCourseAdapter plannedCourseAdapter = new PlannedCourseAdapter(plannedCourseBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(plannedCourseAdapter);
        }
    }

    @Override
    public void showPopMenu(View v) {
        super.showPopMenu(v);
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.exam_score_popmenu, popup.getMenu());
        if (singleSettingData.isHideRepeatScore()) {
            popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示必修课程重复信息");
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.exam_score_top1) {
                if (singleSettingData.isHideRepeatScore()) {
                    singleSettingData.setHideRepeatScore(false);
                    showContent();
                    popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示必修课程重复信息");
                } else {
                    singleSettingData.setHideRepeatScore(true);
                    showContent();
                    popup.getMenu().findItem(R.id.exam_score_top1).setTitle("隐藏必修课程重复信息");
                }
            }
            return true;
        });
        popup.show();
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

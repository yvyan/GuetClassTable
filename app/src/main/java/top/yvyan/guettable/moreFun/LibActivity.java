package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.LibAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.service.table.fetch.StaticService;

import static com.xuexiang.xui.XUI.getContext;

public class LibActivity extends BaseFuncActivity {

    private ScheduleData scheduleData;
    private GeneralData generalData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_lib_schedule));
        openUpdate();

        scheduleData = ScheduleData.newInstance(this);
        generalData = GeneralData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<CourseBean> libBeans = scheduleData.getLibBeans();
        if (libBeans.size() == 0) {
            showEmptyPage();
        } else {
            Collections.sort(libBeans, (courseBean, t1) -> (int) ((courseBean.getLabId() * 100 + courseBean.getWeekStart()) - (t1.getLabId() * 100 + t1.getWeekStart())));
            LibAdapter libAdapter = new LibAdapter(ScheduleSupport.transform(libBeans), generalData.getWeek());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(libAdapter);
        }
    }

    @Override
    public int updateData(String cookie) {
        List<CourseBean> libBeans = StaticService.getLab(this, cookie, generalData.getTerm());
        if (libBeans != null) {
            scheduleData.setLibBeans(libBeans);
            return 5;
        }
        return 1;
    }
}

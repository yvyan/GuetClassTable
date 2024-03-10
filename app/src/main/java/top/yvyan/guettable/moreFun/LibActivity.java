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
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.TimeUtil;

import static com.xuexiang.xui.XUI.getContext;

public class LibActivity extends BaseFuncActivity {

    private GeneralData generalData;
    private TokenData tokenData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_lib_schedule));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_lib_schedule));

        generalData = GeneralData.newInstance(this);
        tokenData = TokenData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<CourseBean> libBeans = ScheduleData.getLibBeans();
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
        List<CourseBean> libBeans = StaticService.getLabTableNew(this, tokenData.getbkjwTestCookie(), TimeUtil.timeFormat3339(generalData.getStartTime()),TimeUtil.timeFormat3339(generalData.getEndTime()));
        if (libBeans != null) {
            ScheduleData.setLibBeans(libBeans);
            ScheduleData.setUpdate(true);
            return 5;
        }
        return 1;
    }
}

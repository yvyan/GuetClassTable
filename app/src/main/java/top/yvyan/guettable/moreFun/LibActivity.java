package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.adapter.LibAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;

import static com.xuexiang.xui.XUI.getContext;

public class LibActivity extends AppCompatActivity implements IMoreFun {

    @BindView(R.id.lib_state)
    TextView libHint;
    @BindView(R.id.lib_not_find)
    View libNotFind;
    @BindView(R.id.lib_info_recycler_view)
    RecyclerView recyclerView;

    ScheduleData scheduleData;
    GeneralData generalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib);
        ButterKnife.bind(this);

        scheduleData = ScheduleData.newInstance(this);
        generalData = GeneralData.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void updateView() {
        List<CourseBean> libBeans = scheduleData.getLibBeans();

        if (libBeans.size() != 0) {
            libNotFind.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            libNotFind.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        Collections.sort(libBeans, (courseBean, t1) -> (int) ((courseBean.getLabId() * 100 + courseBean.getWeekStart()) - (t1.getLabId() * 100 + t1.getWeekStart())));
        LibAdapter libAdapter = new LibAdapter(ScheduleSupport.transform(libBeans), generalData.getWeek());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(libAdapter);
    }

    public void onClick(View view) {
        finish();
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

    @Override
    public void updateView(String hint, int state) {
        libHint.setText(hint);
        if (state == 5) {
            updateView();
            CourseTableFragment.newInstance().updateTable();
        }
    }
}
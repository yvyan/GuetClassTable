package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.SelectedCourseAdapter;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BeanAttributeUtil;

public class SelectedCourseActivity extends BaseFuncActivity implements IMoreFun {

    GeneralData generalData;
    MoreDate moreDate;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_selected_course));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_selected_course));
        setShowMore(false);
        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_selected_course);
        List<SelectedCourseBean> selectedCourseBeans = moreDate.getSelectedCourseBeans();
        RecyclerView rv = findViewById(R.id.rv_selected_course);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new SelectedCourseAdapter(selectedCourseBeans));
    }

    @Override
    public int updateData(String cookie) {
        List<SelectedCourseBean> selectedCourseBeans;
        selectedCourseBeans = StaticService.getSelectedCourse(this, cookie, generalData.getTerm());
        if (selectedCourseBeans != null) {
            BeanAttributeUtil beanAttributeUtil = new BeanAttributeUtil();
            Collections.sort(selectedCourseBeans, beanAttributeUtil);
            moreDate.setSelectedCoursesBeans(selectedCourseBeans);
            update = true;
            return 5;
        }
        return 1;
    }
}
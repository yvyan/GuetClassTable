package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.SelectedCourseAdapter;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.CourseUtil;

public class SelectedCourseActivity extends BaseFuncActivity implements IMoreFun, MaterialSpinner.OnItemSelectedListener<String> {

    private GeneralData generalData;
    private SelectedCourseAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_selected_course));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_selected_course));
        setShowMore(false);
        generalData = GeneralData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_selected_course);
        List<SelectedCourseBean> selectedCourseBeans = MoreData.getSelectedCourseBeans();
        rv = findViewById(R.id.rv_selected_course);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectedCourseAdapter(selectedCourseBeans);
        rv.setAdapter(adapter);

    }

    @Override
    public int updateData(TokenData tokenData) {
        List<SelectedCourseBean> selectedCourseBeans;
        selectedCourseBeans = StaticService.getSelectedCourse(this,tokenData.getbkjwTestCookie() , generalData.getTerm());
        if (selectedCourseBeans != null) {
            CourseUtil.BeanAttributeUtil beanAttributeUtil = new CourseUtil.BeanAttributeUtil();
            Collections.sort(selectedCourseBeans, beanAttributeUtil);
            MoreData.setSelectedCoursesBeans(selectedCourseBeans);
            update = true;
            return 5;
        }
        return 1;
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
    }

}
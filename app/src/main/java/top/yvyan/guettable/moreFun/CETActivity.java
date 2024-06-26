package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.CETAdapter;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.CourseUtil;

import static com.xuexiang.xui.XUI.getContext;

public class CETActivity extends BaseFuncActivity {

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_cet));
        setShowMore(false);
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_cet));
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<CETBean> cetBeans = MoreData.getCetBeans();
        if (cetBeans.size() == 0) {
            showEmptyPage();
        } else {
            CETAdapter cetAdapter = new CETAdapter(cetBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(cetAdapter);
        }
    }

    @Override
    public int updateData(TokenData tokenData) {
        List<CETBean> cetBeans;
        cetBeans = StaticService.getCET(this, tokenData.getBkjwCookie());
        if (cetBeans != null) {
            CourseUtil.BeanAttributeUtil beanAttributeUtil = new CourseUtil.BeanAttributeUtil();
            Collections.sort(cetBeans, beanAttributeUtil);
            if (!AppUtil.equalList(cetBeans, MoreData.getCetBeans())) {
                MoreData.setCetBeans(cetBeans);
            }
            return 5;
        }
        return 1;
    }
}

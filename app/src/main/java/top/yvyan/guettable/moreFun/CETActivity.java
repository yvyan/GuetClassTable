package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.CETAdapter;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;

import static com.xuexiang.xui.XUI.getContext;

public class CETActivity extends BaseFuncActivity {

    private MoreDate moreDate;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_cet));
        setShowMore(false);
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_cet));

        moreDate = MoreDate.newInstance(getApplicationContext());
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<CETBean> cetBeans = moreDate.getCetBeans();
        if (cetBeans.size() == 0) {
            showEmptyPage();
        } else {
            CETAdapter cetAdapter = new CETAdapter(cetBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(cetAdapter);
        }
    }

    @Override
    public int updateData(String cookie) {
        List<CETBean> cetBeans;
        cetBeans = StaticService.getCET(this, cookie);
        if (cetBeans != null) {
            ComparatorBeanAttribute comparatorBeanAttribute = new ComparatorBeanAttribute();
            Collections.sort(cetBeans, comparatorBeanAttribute);
            if (!AppUtil.equalList(cetBeans, moreDate.getCetBeans())) {
                moreDate.setCetBeans(cetBeans);
            }
            return 5;
        }
        return 1;
    }
}

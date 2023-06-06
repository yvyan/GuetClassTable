package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ResitAdapter;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ResitActivity extends BaseFuncActivity {

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_resit_schedule));
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_resit_schedule));
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<ResitBean> resitBeans = MoreData.getResitBeans();
        if (resitBeans.size() == 0) {
            showEmptyPage();
        } else {
            ResitAdapter resitAdapter = new ResitAdapter(resitBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(resitAdapter);
        }
    }

    @Override
    public int updateData(String cookie) {
        List<ResitBean> resitBeans = StaticService.getResit(this, cookie);
        if (resitBeans != null) {
            if (!AppUtil.equalList(resitBeans, MoreData.getResitBeans())) {
                MoreData.setResitBeans(resitBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }
}

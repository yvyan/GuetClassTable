package top.yvyan.guettable.moreFun;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ResitAdapter;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ResitActivity extends BaseFuncActivity {

    private MoreDate moreDate;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_resit_schedule));
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_resit_schedule));

        moreDate = MoreDate.newInstance(getApplicationContext());
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<ResitBean> resitBeans = moreDate.getResitBeans();
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
            if (!AppUtil.equalList(resitBeans, moreDate.getResitBeans())) {
                moreDate.setResitBeans(resitBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }
}

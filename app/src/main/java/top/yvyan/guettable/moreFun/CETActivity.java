package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.CETAdapter;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;

import static com.xuexiang.xui.XUI.getContext;

public class CETActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;
    private boolean update = false;

    @BindView(R.id.CET_state) TextView CET_state;
    @BindView(R.id.CET_not_find) View CET_not_find;
    @BindView(R.id.CET_info_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet);
        ButterKnife.bind(this);

        moreDate = MoreDate.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    /**
     * 更新CET成绩视图
     */
    public void updateView() {
        List<CETBean> cetBeans = moreDate.getCetBeans();
        if (cetBeans.size() != 0) {
            CET_not_find.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            CET_not_find.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        CETAdapter cetAdapter = new CETAdapter(cetBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cetAdapter);
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
                update = true;
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        CET_state.setText(hint);
        if (state == 5 && update) {
            updateView();
        }
    }

    public void onClick(View view) {
        finish();
    }
}
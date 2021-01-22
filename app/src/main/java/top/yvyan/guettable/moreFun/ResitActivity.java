package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ResitAdapter;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;

import static com.xuexiang.xui.XUI.getContext;

public class ResitActivity extends AppCompatActivity implements IMoreFun {

    @BindView(R.id.resit_state)
    TextView resitHint;
    @BindView(R.id.resit_not_find)
    View resitNotFind;
    @BindView(R.id.resit_info_recycler_view)
    RecyclerView recyclerView;

    MoreDate moreDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resit);
        ButterKnife.bind(this);

        moreDate = MoreDate.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void updateView() {
        List<ResitBean> resitBeans = moreDate.getResitBeans();
        if (resitBeans.size() != 0) {
            resitNotFind.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            resitNotFind.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        ResitAdapter resitAdapter = new ResitAdapter(resitBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(resitAdapter);
    }

    @Override
    public int updateData(String cookie) {
        List<ResitBean> resitBeans = StaticService.getResit(this, cookie);
        if (resitBeans != null) {
            moreDate.setResitBeans(resitBeans);
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        resitHint.setText(hint);
        if (state == 5) {
            updateView();
        }
    }

    public void onClick(View view) {
        finish();
    }
}
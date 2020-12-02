package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.CETAdapter;
import top.yvyan.guettable.adapter.ExamAdapter;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ExamUtil;

import static com.xuexiang.xui.XUI.getContext;

public class CETActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;

    private ImageView back;
    private TextView CET_state;
    private TextView CET_not_find;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet);

        moreDate = MoreDate.newInstance(this);

        back = findViewById(R.id.CET_back);
        back.setOnClickListener(view -> {
            finish();
        });
        CET_state = findViewById(R.id.CET_state);
        CET_not_find = findViewById(R.id.CET_not_find);
        recyclerView = findViewById(R.id.CET_info_recycler_view);

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
            moreDate.setCetBeans(cetBeans);
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(int state) {
        switch (state) {
            case 2:
                CET_state.setText("未登录");
                break;
            case -1:
                CET_state.setText("密码错误");
                break;
            case -2:
                CET_state.setText("网络错误");
                break;
            case 91:
                CET_state.setText("登录状态检查");
                break;
            case 92:
                CET_state.setText("正在登录");
                break;
            case 93:
                CET_state.setText("正在更新");
                break;
            case 5:
                CET_state.setText("更新成功");
                updateView();
                break;
            default:
                CET_state.setText("未知错误");
                break;
        }
    }
}
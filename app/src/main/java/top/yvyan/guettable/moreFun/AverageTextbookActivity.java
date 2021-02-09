package top.yvyan.guettable.moreFun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.AvgTextbook;
import top.yvyan.guettable.Gson.AvgTextbookData;
import top.yvyan.guettable.Gson.AvgTextbookFormGet;
import top.yvyan.guettable.Gson.BaseResponse;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.AvgTextbookAdapter;
import top.yvyan.guettable.bean.AvgTextbookBean;
import top.yvyan.guettable.service.table.IMoreFun;
import top.yvyan.guettable.service.table.MoreFunService;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

@SuppressLint("NonConstantResourceId")
public class AverageTextbookActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.textbook_wait)
    View wait;
    @BindView(R.id.textbook_info_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.avg_textbook_start)
    Button start;

    private BaseResponse<List<AvgTextbook>> avgTextbookOuter;
    private List<BaseResponse<AvgTextbookFormGet>> formGetOuters;
    private List<BaseResponse<AvgTextbookData>> dataOuters;
    private String cookie;
    private List<AvgTextbookBean> avgTextbookBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_textbook);
        ButterKnife.bind(this);
        title.setText(getString(R.string.moreFun_evaluating_textbooks));

        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void start() {
        new Thread(() -> {
            int index = 0;
            for (int i = 0; i < dataOuters.size(); i++) {
                if (dataOuters.get(i).getData() == null) {
                    int n = StaticService.averageTextbook(this, cookie, formGetOuters.get(i), avgTextbookOuter.getData().get(i));
                    if (n == 0) {
                        avgTextbookBeans.get(index).setHint("已评价");
                    } else {
                        avgTextbookBeans.get(index).setHint("失败");
                    }
                }
                runOnUiThread(this::updateView);
                index++;
            }
        }).start();
    }

    public void doBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        if ("开始自动评价教材".contentEquals(start.getText())) {
            start.setText("正在评价，请稍后...");
            start();
        }
    }

    @Override
    public int updateData(String cookie) {
        this.cookie = cookie;
        // 获取课表列表
        avgTextbookOuter = StaticService.getTextbookOuter(this, cookie);
        if (avgTextbookOuter != null) {
            avgTextbookBeans = new ArrayList<>();
            formGetOuters = new ArrayList<>();
            dataOuters = new ArrayList<>();
            // 获取评价表
            for (AvgTextbook avgTextbook : avgTextbookOuter.getData()) {
                formGetOuters.add(StaticService.getAvgTextbookFormOuter(this, cookie, avgTextbook));
                dataOuters.add(StaticService.getAvgTextbookDataOuter(this, cookie, avgTextbook));
            }
            for (int i = 0; i < dataOuters.size(); i++) {
                String courseName = avgTextbookOuter.getData().get(i).getCname();
                String textbookName = avgTextbookOuter.getData().get(i).getName();
                String hint = dataOuters.get(i).getData() == null ? "未评价" : "已评价";
                avgTextbookBeans.add(new AvgTextbookBean(courseName, textbookName, hint));
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int stateNum) {
        this.state.setText(hint);
        if (stateNum == 5) {
            updateView();
        } else if (stateNum == 2 || stateNum == -1 || stateNum == -2 || stateNum == -3) {
            View loading = findViewById(R.id.page_loading);
            View fail = findViewById(R.id.page_fail);
            loading.setVisibility(View.GONE);
            fail.setVisibility(View.VISIBLE);
        }
    }

    public void updateView() {
        wait.setVisibility(View.GONE);
        AvgTextbookAdapter avgTextbookAdapter = new AvgTextbookAdapter(avgTextbookBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(avgTextbookAdapter);
        if (avgTextbookBeans.size() == 0 || (!"".equals(avgTextbookBeans.get(avgTextbookBeans.size() - 1).getHint()) && "正在评价，请稍后...".contentEquals(start.getText()))) {
            start.setText("开始自动评价教材");
            ToastUtil.showToast(this, "教材评价已经完成！");
        }
    }
}
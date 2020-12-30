package top.yvyan.guettable.moreFun;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.AvgTextbook;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.AvgTextbookAdapter;
import top.yvyan.guettable.bean.AvgTextbookBean;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

// 未完成
public class AverageTextbookActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.average_textbook_state)
    TextView title;
    @BindView(R.id.textbook_wait)
    TextView wait;
    @BindView(R.id.textbook_info_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.avg_textbook_start)
    Button start;

    private List<AvgTextbook> avgTextbooks;
    private String cookie;
    private List<AvgTextbookBean> avgTextbookBeans;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_textbook);
        ButterKnife.bind(this);

        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void start() {
        new Thread(() -> {
            for (AvgTextbook avgTextbook : avgTextbooks) {
                if (avgTextbook == null) {
//                    int n = StaticService.averageTeacher(this, cookie, avgTeacher, GeneralData.newInstance(this).getNumber());
                    int n = 0;
                    if (n == 0) {
                        avgTextbookBeans.get(index).setHint("已评");
                    } else {
                        avgTextbookBeans.get(index).setHint("失败");
                    }
                }
                runOnUiThread(() -> {
                    updateView();
                });
                index++;
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.average_textbook_back:
                finish();
                break;
            case R.id.avg_textbook_start:
                if ("更新成功".equals(title.getText()) && "开始自动评价教材".equals(start.getText())) {
                    start.setText("正在评价，请稍后...");
                    start();
                }
            default:
                break;
        }
    }

    @Override
    public int updateData(String cookie) {
        this.cookie = cookie;
        avgTextbooks = StaticService.getTextbookList(this, cookie);
        if (avgTextbooks != null) {
            avgTextbookBeans = new ArrayList<>();
            for (AvgTextbook avgTextbook : avgTextbooks) {
//                avgTextbookBeans.add(new AvgTextbookBean(avgTextbook.getCname(), avgTextbook.getName(), ((avgTextbook.getChk() == 1) ? "已评教" : "")));
                // 需要判断是否已经评教并且对avgTextbookBeans进行数据填充
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        title.setText(hint);
        if (state == 5) {
            updateView();
        }
    }

    public void updateView() {
        wait.setVisibility(View.GONE);
        AvgTextbookAdapter avgTextbookAdapter = new AvgTextbookAdapter(avgTextbookBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(avgTextbookAdapter);
        if (!"".equals(avgTextbookBeans.get(avgTextbookBeans.size() - 1).getHint()) && "正在评价，请稍后...".equals(start.getText())) {
            start.setText("开始自动评价教材");
            ToastUtil.showToast(this, "评价已经完成！");
        }
    }
}
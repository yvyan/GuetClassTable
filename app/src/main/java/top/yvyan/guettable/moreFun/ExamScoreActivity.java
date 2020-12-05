package top.yvyan.guettable.moreFun;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamScoreAdapter;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ComparatorExamScore;

import static com.xuexiang.xui.XUI.getContext;

public class ExamScoreActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;

    private ImageView back;
    private TextView examScoreState;
    private TextView examScoreNotFind;
    private ImageView examScoreMore;
    private View examScoreInfoView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examscore);

        moreDate = MoreDate.newInstance(this);

        back = findViewById(R.id.examscore_back);
        back.setOnClickListener(view -> {finish();});

        examScoreState = findViewById(R.id.examscore_state);
        examScoreNotFind = findViewById(R.id.examscore_not_find);
        examScoreMore = findViewById(R.id.examscore_more);
        examScoreMore.setOnClickListener(view -> showPopMenu());
        examScoreInfoView = findViewById(R.id.examscore_info_view);
        recyclerView = findViewById(R.id.examscore_info_recycler_view);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    /**
     * 更新考试成绩视图
     */
    public void updateView() {
        List<ExamScoreBean> examScoreBeans = moreDate.getExamScoreBeans();
        if (examScoreBeans.size() != 0) {
            examScoreNotFind.setVisibility(View.GONE);
            examScoreInfoView.setVisibility(View.VISIBLE);
        } else {
            examScoreNotFind.setVisibility(View.VISIBLE);
            examScoreInfoView.setVisibility(View.GONE);
        }
        ExamScoreAdapter examScoreAdapter = new ExamScoreAdapter(examScoreBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(examScoreAdapter);
    }

    public void showPopMenu() { }

    @Override
    public int updateData(String cookie) {
        List<ExamScoreBean> examScoreBeans;
        examScoreBeans = StaticService.getExamScore(this, cookie);
        if (examScoreBeans != null) {
            ComparatorExamScore comparatorExamScore = new ComparatorExamScore();
            Collections.sort(examScoreBeans, comparatorExamScore);
            moreDate.setExamScoreBeans(examScoreBeans);
            return 5 ;
        }
        return 1;
    }

    @Override
    public void updateView(int state) {
        switch (state) {
            case 2:
                examScoreState.setText("未登录");
                break;
            case -1:
                examScoreState.setText("密码错误");
                break;
            case -2:
                examScoreState.setText("网络错误");
                break;
            case 91:
                examScoreState.setText("登录状态检查");
                break;
            case 92:
                examScoreState.setText("正在登录");
                break;
            case 93:
                examScoreState.setText("正在更新");
                break;
            case 5:
                examScoreState.setText("更新成功");
                updateView();
                break;
            default:
                examScoreState.setText("未知错误");
                break;
        }
    }
}

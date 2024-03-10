package top.yvyan.guettable.moreFun;

import static com.xuexiang.xui.XUI.getContext;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import top.yvyan.guettable.Gson.ExamScoreNew;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamScoreNewAdapter;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

public class ExamScoreNewActivity extends BaseFuncActivity {


    private ExamScoreNew scoreData;
    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_test_scores_new));
        setShowMore(false);
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_test_scores_new));

    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_exam_score_new);
        RecyclerView recyclerView = findViewById(R.id.exam_score_info_recycler_view);
        if (scoreData==null || scoreData.semesterId2studentGrades.isEmpty()) {
            showEmptyPage();
        } else {
            ExamScoreNewAdapter examScoreAdapter = new ExamScoreNewAdapter(scoreData);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(examScoreAdapter);
        }
    }

    @Override
    public int updateData(TokenData tokenData) {
        scoreData = StaticService.getExamScoreNew(this, tokenData.getbkjwTestCookie());
        if (scoreData != null) {
            update = true;
            return 5;
        }
        return 1;
    }
}

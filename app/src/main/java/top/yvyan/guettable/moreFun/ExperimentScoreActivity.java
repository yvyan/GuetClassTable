package top.yvyan.guettable.moreFun;

import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExperimentScoreAdapter;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.CourseUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExperimentScoreActivity extends BaseFuncActivity {

    private GeneralData generalData;
    private SingleSettingData singleSettingData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_lib_scores));
        setShowMore(true);
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_lib_scores));

        generalData = GeneralData.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_experiment_score);
        RecyclerView recyclerView = findViewById(R.id.experiment_score_info_recycler_view);
        List<ExperimentScoreBean> experimentScoreBeans = MoreData.getExperimentScoreBeans();
        if (singleSettingData.isHideOtherTermExamScore()) {
            experimentScoreBeans = CourseUtil.BeanAttributeUtil.hideOtherTerm(experimentScoreBeans, generalData.getTerm());
        }
        if (experimentScoreBeans.size() == 0) {
            showEmptyPage();
        } else {
            ExperimentScoreAdapter experimentScoreAdapter = new ExperimentScoreAdapter(experimentScoreBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(experimentScoreAdapter);
        }
    }

    @Override
    protected void showPopMenu(View v) {
        super.showPopMenu(v);
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.exam_score_popmenu, popup.getMenu());
        if (singleSettingData.isHideOtherTermExamScore()) {
            popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示其它学期成绩");
        }
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.exam_score_top1) {
                if (singleSettingData.isHideOtherTermExamScore()) {
                    singleSettingData.setHideOtherTermExamScore(false);
                    showContent();
                    popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示其它学期成绩");
                } else {
                    singleSettingData.setHideOtherTermExamScore(true);
                    showContent();
                    popup.getMenu().findItem(R.id.exam_score_top1).setTitle("隐藏其它学期成绩");
                }
            }
            return true;
        });
        popup.show();
    }

    @Override
    public int updateData(TokenData tokenData) {
        List<ExperimentScoreBean> experimentScoreBeans;
        experimentScoreBeans = StaticService.getExperimentScore(this, tokenData.getBkjwCookie());
        if (experimentScoreBeans != null) {
            CourseUtil.BeanAttributeUtil beanAttributeUtil = new CourseUtil.BeanAttributeUtil();
            Collections.sort(experimentScoreBeans, beanAttributeUtil);
            if (!AppUtil.equalList(experimentScoreBeans, MoreData.getExperimentScoreBeans())) {
                MoreData.setExperimentScoreBeans(experimentScoreBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }
}

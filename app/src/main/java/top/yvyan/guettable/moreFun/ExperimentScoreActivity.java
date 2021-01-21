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

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExperimentScoreAdapter;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BeanHideUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;

import static com.xuexiang.xui.XUI.getContext;

public class ExperimentScoreActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;
    private GeneralData generalData;
    private SingleSettingData singleSettingData;
    private boolean update = false;

    @BindView(R.id.experimentscore_state) TextView experimentScoreState;
    @BindView(R.id.experimentscore_not_find) View experimentScoreNotFind;
    @BindView(R.id.experiment_score_more) ImageView experimentScoreMore;
    @BindView(R.id.experimentscore_info_view) View experimentScoreInfoView;
    @BindView(R.id.experimentscore_info_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimentscore);
        ButterKnife.bind(this);

        moreDate = MoreDate.newInstance(this);
        generalData = GeneralData.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    /**
     * 更新考试成绩视图
     */
    public void updateView() {
        List<ExperimentScoreBean> experimentScoreBeans = moreDate.getExperimentScoreBeans();
        if (singleSettingData.isHideOtherTermExamScore()) {
            experimentScoreBeans = BeanHideUtil.hideOtherTermExamScore(experimentScoreBeans, generalData.getTerm());
        }
        if (experimentScoreBeans.size() != 0) {
            experimentScoreNotFind.setVisibility(View.GONE);
            experimentScoreInfoView.setVisibility(View.VISIBLE);
        } else {
            experimentScoreNotFind.setVisibility(View.VISIBLE);
            experimentScoreInfoView.setVisibility(View.GONE);
        }
        ExperimentScoreAdapter experimentScoreAdapter = new ExperimentScoreAdapter(experimentScoreBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(experimentScoreAdapter);
    }

    public void onClick(View view) {
        finish();
    }

    public void showPopMenu(View view) {
        PopupMenu popup = new PopupMenu(this, experimentScoreMore);
        popup.getMenuInflater().inflate(R.menu.exam_score_popmenu, popup.getMenu());
        if (singleSettingData.isHideOtherTermExamScore()) {
            popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示其它学期成绩");
        }
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.exam_score_top1:
                    if (singleSettingData.isHideOtherTermExamScore()) {
                        singleSettingData.setHideOtherTermExamScore(false);
                        updateView();
                        popup.getMenu().findItem(R.id.exam_score_top1).setTitle("显示其它学期成绩");
                    } else {
                        singleSettingData.setHideOtherTermExamScore(true);
                        updateView();
                        popup.getMenu().findItem(R.id.exam_score_top1).setTitle("隐藏其它学期成绩");
                    }
                    break;
                default:
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    public int updateData(String cookie) {
        List<ExperimentScoreBean> experimentScoreBeans;
        experimentScoreBeans = StaticService.getExperimentScore(this, cookie);
        if (experimentScoreBeans != null) {
            ComparatorBeanAttribute comparatorBeanAttribute = new ComparatorBeanAttribute();
            Collections.sort(experimentScoreBeans, comparatorBeanAttribute);
            if (!AppUtil.equalList(experimentScoreBeans, moreDate.getExperimentScoreBeans())) {
                moreDate.setExperimentScoreBeans(experimentScoreBeans);
                update = true;
            }
            return 5 ;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        experimentScoreState.setText(hint);
        if (state == 5 && update) {
            updateView();
        }
    }
}

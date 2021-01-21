package top.yvyan.guettable.moreFun;

import android.os.Bundle;
import android.util.Log;
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
import top.yvyan.guettable.adapter.ExamScoreAdapter;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;
import top.yvyan.guettable.util.BeanHideUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamScoreActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    private MoreDate moreDate;
    private GeneralData generalData;
    private SingleSettingData singleSettingData;
    private boolean update = false;

    @BindView(R.id.examscore_state) TextView examScoreState;
    @BindView(R.id.examscore_not_find) TextView examScoreNotFind;
    @BindView(R.id.exam_score_more) ImageView examScoreMore;
    @BindView(R.id.examscore_info_view) View examScoreInfoView;
    @BindView(R.id.examscore_info_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examscore);
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
        List<ExamScoreBean> examScoreBeans = moreDate.getExamScoreBeans();
        if (singleSettingData.isHideOtherTermExamScore()) {
            examScoreBeans = BeanHideUtil.hideOtherTermExamScore(examScoreBeans, generalData.getTerm());
        }
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

    public void showPopMenu(View view) {
        PopupMenu popup = new PopupMenu(this, examScoreMore);
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
        List<ExamScoreBean> examScoreBeans;
        examScoreBeans = StaticService.getExamScore(this, cookie);
        if (examScoreBeans != null) {
            ComparatorBeanAttribute comparatorBeanAttribute = new ComparatorBeanAttribute();
            Collections.sort(examScoreBeans, comparatorBeanAttribute);
            if (!AppUtil.equalList(examScoreBeans, moreDate.getExamScoreBeans())) {
                moreDate.setExamScoreBeans(examScoreBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        examScoreState.setText(hint);
        if (state == 5 && update) {
            updateView();
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}

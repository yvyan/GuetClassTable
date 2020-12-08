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
import top.yvyan.guettable.adapter.ExperimentScoreAdapter;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.BeanHideUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;

import static com.xuexiang.xui.XUI.getContext;

public class ExperimentScoreActivity extends AppCompatActivity implements IMoreFun {

    private MoreDate moreDate;
    private GeneralData generalData;
    private SingleSettingData singleSettingData;

    private ImageView back;
    private TextView experimentScoreState;
    private TextView experimentScoreNotFind;
    private ImageView experimentScoreMore;
    private View experimentScoreInfoView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimentscore);

        moreDate = MoreDate.newInstance(this);
        generalData = GeneralData.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);

        back = findViewById(R.id.experimentscore_back);
        back.setOnClickListener(view -> {finish();});

        experimentScoreState = findViewById(R.id.experimentscore_state);
        experimentScoreNotFind = findViewById(R.id.experimentscore_not_find);
        experimentScoreMore = findViewById(R.id.experiment_score_more);
        experimentScoreMore.setOnClickListener(view -> showPopMenu());
        experimentScoreInfoView = findViewById(R.id.experimentscore_info_view);
        recyclerView = findViewById(R.id.experimentscore_info_recycler_view);

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

    public void showPopMenu() {
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
            moreDate.setExperimentScoreBeans(experimentScoreBeans);
            return 5 ;
        }
        return 1;
    }

    @Override
    public void updateView(int state) {
        switch (state) {
            case 2:
                experimentScoreState.setText("未登录");
                break;
            case -1:
                experimentScoreState.setText("密码错误");
                break;
            case -2:
                experimentScoreState.setText("网络错误");
                break;
            case 91:
                experimentScoreState.setText("登录状态检查");
                break;
            case 92:
                experimentScoreState.setText("正在登录");
                break;
            case 93:
                experimentScoreState.setText("正在更新");
                break;
            case 5:
                experimentScoreState.setText("更新成功");
                updateView();
                break;
            default:
                experimentScoreState.setText("未知错误");
                break;
        }
    }
}

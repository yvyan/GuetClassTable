package top.yvyan.guettable.moreFun;

import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamScoreAdapter;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.CourseUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamScoreActivity extends BaseFuncActivity {

    private MoreDate moreDate;
    private GeneralData generalData;
    private SingleSettingData singleSettingData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_test_scores));
        setShowMore(true);
        openUpdate();
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_test_scores));

        moreDate = MoreDate.newInstance(this);
        generalData = GeneralData.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_exam_score);
        RecyclerView recyclerView = findViewById(R.id.exam_score_info_recycler_view);
        List<ExamScoreBean> examScoreBeans = moreDate.getExamScoreBeans();
        if (singleSettingData.isHideOtherTermExamScore()) {
            examScoreBeans = CourseUtil.BeanAttributeUtil.hideOtherTerm(examScoreBeans, generalData.getTerm());
        }
        if (examScoreBeans.size() == 0) {
            showEmptyPage();
        } else {
            ExamScoreAdapter examScoreAdapter = new ExamScoreAdapter(examScoreBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(examScoreAdapter);
        }
    }

    @Override
    public void showPopMenu(View v) {
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
    public int updateData(String cookie) {
        List<ExamScoreBean> examScoreBeans;
        examScoreBeans = StaticService.getExamScore(this, cookie);
        if (examScoreBeans != null) {
            CourseUtil.BeanAttributeUtil beanAttributeUtil = new CourseUtil.BeanAttributeUtil();
            Collections.sort(examScoreBeans, beanAttributeUtil);
            if (!AppUtil.equalList(examScoreBeans, moreDate.getExamScoreBeans())) {
                moreDate.setExamScoreBeans(examScoreBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }
}

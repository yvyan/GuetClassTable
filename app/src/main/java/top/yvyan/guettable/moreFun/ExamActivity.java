package top.yvyan.guettable.moreFun;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ComparatorBeanAttribute;
import top.yvyan.guettable.util.ExamUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamActivity extends BaseFuncActivity {

    private GeneralData generalData;
    private ScheduleData scheduleData;
    private SingleSettingData singleSettingData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_test_schedule));
        setShowMore(true);
        openUpdate();

        generalData = GeneralData.newInstance(this);
        scheduleData = ScheduleData.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.recycler_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_info);
        List<ExamBean> examBeans = scheduleData.getExamBeans();
        if (singleSettingData.isCombineExam()) {
            examBeans = ExamUtil.combineExam(examBeans);
        }
        if (singleSettingData.isHideOutdatedExam()) {
            examBeans = ExamUtil.ridOfOutdatedExam(examBeans);
        }
        if (examBeans.size() == 0) {
            showEmptyPage();
        } else {
            ExamAdapter examAdapter = new ExamAdapter(examBeans);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(examAdapter);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void showPopMenu(View v) {
        super.showPopMenu(v);
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.exam_popmenu, popup.getMenu());
        if (singleSettingData.isCombineExam()) {
            popup.getMenu().findItem(R.id.exam_top1).setTitle("不合并考试安排");
        }
        if (singleSettingData.isHideOutdatedExam()) {
            popup.getMenu().findItem(R.id.exam_top2).setTitle("显示过期的考试安排");
        }
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.exam_top1:
                    if (singleSettingData.isCombineExam()) {
                        singleSettingData.setCombineExam(false);
                        showContent();
                        popup.getMenu().findItem(R.id.exam_top1).setTitle("合并考试安排");
                    } else {
                        singleSettingData.setCombineExam(true);
                        showContent();
                        popup.getMenu().findItem(R.id.exam_top1).setTitle("不合并考试安排");
                    }
                    break;
                case R.id.exam_top2:
                    if (singleSettingData.isHideOutdatedExam()) {
                        singleSettingData.setHideOutdatedExam(false);
                        showContent();
                        popup.getMenu().findItem(R.id.exam_top2).setTitle("隐藏过期的考试安排");
                    } else {
                        singleSettingData.setHideOutdatedExam(true);
                        showContent();
                        popup.getMenu().findItem(R.id.exam_top2).setTitle("显示过期的考试安排");
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
        List<ExamBean> examBeans;
        examBeans = StaticService.getExam(this, cookie, generalData.getTerm());
        if (examBeans != null) {
            ComparatorBeanAttribute comparatorBeanAttribute = new ComparatorBeanAttribute();
            Collections.sort(examBeans, comparatorBeanAttribute);
            if (!AppUtil.equalList(examBeans, scheduleData.getExamBeans())) {
                scheduleData.setExamBeans(examBeans);
                update = true;
            }
            return 5;
        }
        return 1;
    }
}

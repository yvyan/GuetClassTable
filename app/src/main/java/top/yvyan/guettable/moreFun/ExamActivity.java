package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ComparatorBeanAttribute;
import top.yvyan.guettable.util.ExamUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamActivity extends AppCompatActivity implements IMoreFun {

    private GeneralData generalData;
    private MoreDate moreDate;
    private SingleSettingData singleSettingData;

    @BindView(R.id.exam_state) TextView examState;
    @BindView(R.id.exam_not_find) TextView examNotFind;
    @BindView(R.id.exam_more) ImageView examMore;
    @BindView(R.id.exam_info_recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);

        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);

        examMore.setOnClickListener(view -> showPopMenu());

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    /**
     * 更新考试安排视图
     */
    public void updateView() {
        List<ExamBean> examBeans = moreDate.getExamBeans();
        if (singleSettingData.isCombineExam()) {
            examBeans = ExamUtil.combineExam(examBeans);
        }
        if (singleSettingData.isHideOutdatedExam()) {
            examBeans = ExamUtil.ridOfOutdatedExam(examBeans);
        }
        if (examBeans.size() != 0) {
            examNotFind.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            examNotFind.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        ExamAdapter examAdapter = new ExamAdapter(examBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(examAdapter);
    }

    /**
     * 显示弹出菜单
     */
    public void showPopMenu() {
        PopupMenu popup = new PopupMenu(this, examMore);
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
                        updateView();
                        popup.getMenu().findItem(R.id.exam_top1).setTitle("合并考试安排");
                    } else {
                        singleSettingData.setCombineExam(true);
                        updateView();
                        popup.getMenu().findItem(R.id.exam_top1).setTitle("不合并考试安排");
                    }
                    break;
                case R.id.exam_top2:
                    if (singleSettingData.isHideOutdatedExam()) {
                        singleSettingData.setHideOutdatedExam(false);
                        updateView();
                        popup.getMenu().findItem(R.id.exam_top2).setTitle("隐藏过期的考试安排");
                    } else {
                        singleSettingData.setHideOutdatedExam(true);
                        updateView();
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
            moreDate.setExamBeans(examBeans);
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        examState.setText(hint);
        if (state == 5) {
            updateView();
            CourseTableFragment.newInstance().updateTable();
        }
    }


    public void onClick(View view) {
        finish();
    }
}
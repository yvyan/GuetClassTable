package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.yvyan.guettable.adapter.ExamAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ExamUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamActivity extends AppCompatActivity {

    private AccountData accountData;
    private GeneralData generalData;
    private MoreDate moreDate;
    private SingleSettingData singleSettingData;

    private TextView examState;
    private TextView examNotFind;
    private ImageView examMore;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        accountData = AccountData.newInstance(this);
        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);

        examState = findViewById(R.id.exam_state);
        examNotFind = findViewById(R.id.exam_not_find);
        examMore = findViewById(R.id.exam_more);
        examMore.setOnClickListener(view -> showPopMenu());
        recyclerView = findViewById(R.id.exam_info_recycler_view);

        updateView();

        updateExam();
    }

    /**
     * 获取考试安排信息
     */
    private void updateExam() {
        new Thread(() -> {
            List<ExamBean> examBeans;
            StringBuilder cookie_builder = new StringBuilder();
            if (accountData.getIsLogin()) {
                int state = StaticService.autoLogin(
                        this,
                        accountData.getUsername(),
                        accountData.getPassword(),
                        cookie_builder
                );
                if (state == 0) {
                    examBeans = StaticService.getExam(this, cookie_builder.toString(), generalData.getTerm());
                    if (examBeans != null) {
                        moreDate.setExamBeans(examBeans);
                        runOnUiThread(() -> {
                            examState.setText("考试安排 更新成功");
                            updateView();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        examState.setText("考试安排 网络错误，从本地导入");
                        updateView();
                    });
                }
            } else {
                runOnUiThread(() -> examState.setText("考试安排 未登录，从本地导入"));
            }
        }).start();
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
}
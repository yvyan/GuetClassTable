package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ExamAdapter;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ExamUtil;

import static com.xuexiang.xui.XUI.getContext;

public class ExamActivity extends AppCompatActivity implements IMoreFun {

    private GeneralData generalData;
    private MoreDate moreDate;
    private SingleSettingData singleSettingData;

    private ImageView back;
    private TextView examState;
    private TextView examNotFind;
    private ImageView examMore;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);
        singleSettingData = SingleSettingData.newInstance(this);

        back = findViewById(R.id.exam_back);
        back.setOnClickListener(view -> {
            finish();
        });
        examState = findViewById(R.id.exam_state);
        examNotFind = findViewById(R.id.exam_not_find);
        examMore = findViewById(R.id.exam_more);
        examMore.setOnClickListener(view -> showPopMenu());
        recyclerView = findViewById(R.id.exam_info_recycler_view);

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
            moreDate.setExamBeans(examBeans);
            return 5;
        }
        return 1;
    }
    /**
     * state记录当前状态
     *  0 : 登录成功
     *  1 : 登录失效
     *  2 : 未登录
     *
     *  5 : 通用获取数据成功
     *
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     *
     * 21 : 理论课更新成功
     * 22 : 课内实验更新成功
     * 23 : 考试安排更新成功
     *
     * 91 : 登录状态检查
     * 92 : 正在登录
     * 93 : 正在更新
     *
     */
    @Override
    public void updateView(int state) {
        switch (state) {
            case 2:
                examState.setText("未登录");
                break;
            case -1:
                examState.setText("密码错误");
                break;
            case -2:
                examState.setText("网络错误");
                break;
            case 91:
                examState.setText("登录状态检查");
                break;
            case 92:
                examState.setText("正在登录");
                break;
            case 93:
                examState.setText("正在更新");
                break;
            case 5:
                examState.setText("更新成功");
                updateView();
                break;
            default:
                examState.setText("未知错误");
                break;
        }
    }
}
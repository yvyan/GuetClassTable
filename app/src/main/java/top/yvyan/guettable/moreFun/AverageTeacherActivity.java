package top.yvyan.guettable.moreFun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umeng.umcrash.UMCrash;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.AvgTeacher;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.AvgTeacherAdapter;
import top.yvyan.guettable.bean.AvgTeacherBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

@SuppressLint("NonConstantResourceId")
public class AverageTeacherActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.teacher_waite)
    View waite;
    @BindView(R.id.teacher_info_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.avg_teacher_start)
    Button start;
    @BindView(R.id.func_base_constraintLayout)
    ConstraintLayout header;

    private List<AvgTeacher> avgTeachers;
    private String cookie;
    private List<AvgTeacherBean> avgTeacherBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(this);
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_average_teacher);
        ButterKnife.bind(this);
        header.getBackground().setAlpha(255);
        title.setText(getString(R.string.moreFun_evaluating_teachers));
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_evaluating_teachers));

        BackgroundUtil.setFullAlphaStatus(this);

        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    @Override
    public int updateData(String cookie) {
        this.cookie = cookie;
        avgTeachers = StaticService.getTeacherList(this, cookie, null);
        if (avgTeachers != null) {
            avgTeacherBeans = new ArrayList<>();
            for (AvgTeacher avgTeacher : avgTeachers) {
                avgTeacherBeans.add(new AvgTeacherBean(avgTeacher.getCname(), avgTeacher.getName(), ((avgTeacher.getChk() == 1) ? "已评教" : "")));
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int stateNum) {
        this.state.setText(hint);
        if (stateNum == 5) {
            updateView();
        } else if (stateNum == 2 || stateNum == -1 || stateNum == -2 || stateNum == -3) {
            View loading = findViewById(R.id.page_loading);
            View fail = findViewById(R.id.page_fail);
            loading.setVisibility(View.GONE);
            fail.setVisibility(View.VISIBLE);
        }
    }

    public void updateView() {
        waite.setVisibility(View.GONE);
        AvgTeacherAdapter avgTeacherAdapter = new AvgTeacherAdapter(avgTeacherBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(avgTeacherAdapter);
        if (avgTeacherBeans.size() == 0 || (!"".equals(avgTeacherBeans.get(avgTeacherBeans.size() - 1).getHint()) && "正在评价，请稍后...".contentEquals(start.getText()))) {
            start.setText("开始自动评价教师");
            ToastUtil.showToast(this, "评价教师已经完成！");
        }
    }

    private void start() {
        new Thread(() -> {
            try {
                int index = 0;
                for (AvgTeacher avgTeacher : avgTeachers) {
                    try {
                        if (avgTeacher.getChk() == 0) {
                            int n = StaticService.averageTeacher(this, cookie, avgTeacher, GeneralData.newInstance(this).getNumber());
                            if (n == 0) {
                                avgTeacherBeans.get(index).setHint("已评教");
                            } else {
                                n = StaticService.averageTeacher(this, cookie, avgTeacher, GeneralData.newInstance(this).getNumber());
                                if (n != 0) {
                                    avgTeacherBeans.get(index).setHint("失败");
                                } else {
                                    avgTeacherBeans.get(index).setHint("已评教");
                                }
                            }
                        }
                    } catch (Exception e) {
                        avgTeacherBeans.get(index).setHint("失败");
                        UMCrash.generateCustomLog(e, "avgTeacher");
                    }
                    runOnUiThread(this::updateView);
                    index++;
                }
            } catch (Exception e) {
                UMCrash.generateCustomLog(e, "AverageTeacher");
            }
        }).start();
    }

    public void doBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        if ("开始自动评价教师".contentEquals(start.getText())) {
            start.setText("正在评价，请稍后...");
            start();
        }
    }
}
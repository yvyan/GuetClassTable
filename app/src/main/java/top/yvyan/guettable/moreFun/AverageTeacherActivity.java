package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.AvgTeacher;
import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.AvgTeacherAdapter;
import top.yvyan.guettable.bean.AvgTeacherBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

import static com.xuexiang.xui.XUI.getContext;

public class AverageTeacherActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.average_teacher_state) TextView title;
    @BindView(R.id.teacher_waite) View waite;
    @BindView(R.id.teacher_info_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.avg_teacher_start) Button start;

    private List<AvgTeacher> avgTeachers;
    private String cookie;
    private List<AvgTeacherBean> avgTeacherBeans;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_teacher);
        ButterKnife.bind(this);

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
                avgTeacherBeans.add(new AvgTeacherBean(avgTeacher.getCname(), avgTeacher.getName(), ((avgTeacher.getChk() == 1)? "已评教" : "")));
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        title.setText(hint);
        if (state == 5) {
            updateView();
        }
    }

    public void updateView() {
        waite.setVisibility(View.GONE);
        AvgTeacherAdapter avgTeacherAdapter = new AvgTeacherAdapter(avgTeacherBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(avgTeacherAdapter);
        if (!"".equals(avgTeacherBeans.get(avgTeacherBeans.size() - 1).getHint()) && "正在评价，请稍后...".equals(start.getText())) {
            start.setText("开始自动评价教师");
            ToastUtil.showToast(this, "评教已经完成！");
        }
    }

    private void start() {
        new Thread(() -> {
            for (AvgTeacher avgTeacher : avgTeachers) {
                Log.d("", avgTeacher.getName());
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
                runOnUiThread(() -> {
                    updateView();
                });
                index ++;
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.average_teacher_back:
                finish();
                break;
            case R.id.avg_teacher_start:
                if ("更新成功".equals(title.getText()) && "开始自动评价教师".equals(start.getText())) {
                    start.setText("正在评价，请稍后...");
                    start();
                }
            default:
                break;
        }
    }
}
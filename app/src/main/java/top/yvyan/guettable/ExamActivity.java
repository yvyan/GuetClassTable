package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.GetDataService;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

public class ExamActivity extends AppCompatActivity {

    private AccountData accountData;
    private GeneralData generalData;
    private MoreDate moreDate;

    private TextView examState;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        accountData = AccountData.newInstance(this);
        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);

        examState = findViewById(R.id.exam_state);
        textView = findViewById(R.id.testShow);

        updateView();

        new Thread(() -> {
            List<ExamBean> examBeans;
            String cookie = GetDataService.autoLogin(
                    this,
                    accountData.getUsername(),
                    accountData.getPassword(),
                    generalData.getTerm()
            );
            if (cookie != null) {
                examBeans = GetDataService.getExam(this, cookie, generalData.getTerm());
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
        }).start();
    }

    /**
     * 更新考试安排视图
     */
    public void updateView() {
        List<ExamBean> examBeans = moreDate.getExamBeans();
        textView.setText(examBeans.toString());
    }
}
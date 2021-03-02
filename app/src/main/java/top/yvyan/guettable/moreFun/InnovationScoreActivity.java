package top.yvyan.guettable.moreFun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.button.ButtonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.Gson.BaseResponse;
import top.yvyan.guettable.Gson.InnovationScore;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.table.IMoreFun;
import top.yvyan.guettable.service.table.MoreFunService;
import top.yvyan.guettable.service.table.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.ToastUtil;

@SuppressLint("NonConstantResourceId")
public class InnovationScoreActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.state)
    TextView innovationScoreState;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.innovation_dptName)
    TextView innovation_dptName;
    @BindView(R.id.innovation_major)
    TextView innovation_major;
    @BindView(R.id.innovation_grade)
    TextView innovation_grade;
    @BindView(R.id.innovation_studentNumber)
    TextView innovation_studentNumber;
    @BindView(R.id.innovation_studentName)
    TextView innovation_studentName;
    @BindView(R.id.innovation_basicScore)
    TextView innovationScore_Basic;
    @BindView(R.id.innovation_basicScore_train)
    TextView innovationScore_Train;
    @BindView(R.id.innovation_basicScore_course)
    TextView innovationScore_Course;
    @BindView(R.id.innovation_practise)
    TextView innovationScore_pratise;
    @BindView(R.id.innovation_lack)
    TextView innovationScore_Lack;
    @BindView(R.id.innovation_btn_update)
    ButtonView btn_update_innovationScore;

    private BaseResponse<InnovationScore> innovationScoreBaseResponse;
    private MoreFunService moreFunService;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(getApplicationContext());
        setPageTheme(singleSettingData.getThemeId());
        setContentView(R.layout.activity_innovation_score);
        ButterKnife.bind(this);
        title.setText(getString(R.string.moreFun_innovation_score));
        btn_update_innovationScore.setOnClickListener(this);
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_innovation_score));

        moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.innovation_btn_update:
                new Thread(() -> {
                    runOnUiThread(() -> ToastUtil.showToast(this, "正在更新，请耐心等候"));
                    int result = StaticService.updateInnovationScore(this, cookie);
                    if (result == 0) {
                        moreFunService.update();
                        runOnUiThread(() -> ToastUtil.showToast(this, "更新数据成功"));
                    } else {
                        runOnUiThread(() -> ToastUtil.showToast(this, "更新失败，请稍后重试"));
                    }
                }).start();
                break;
        }
    }

    @Override
    public int updateData(String cookie) {
        this.cookie = cookie;
        innovationScoreBaseResponse = StaticService.getInnovationScore(this, cookie);
        if (innovationScoreBaseResponse != null) {
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        innovationScoreState.setText(hint);
        if (state == 5) {
            updateView();
        } else if (state == 2 || state == -1 || state == -2 || state == -3) {
            View loading = findViewById(R.id.page_loading);
            View fail = findViewById(R.id.page_fail);
            loading.setVisibility(View.GONE);
            fail.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        View wait = findViewById(R.id.innovation_waite);
        wait.setVisibility(View.GONE);
        InnovationScore innovationScore = innovationScoreBaseResponse.getData();
        innovation_dptName.setText(innovationScore.getDptname());
        innovation_grade.setText(innovationScore.getGrade());
        innovation_major.setText(innovationScore.getSpname());
        innovation_studentName.setText(innovationScore.getName());
        innovation_studentNumber.setText(innovationScore.getStid());
        innovationScore_Basic.setText(String.valueOf(innovationScore.getLb1()));
        innovationScore_Train.setText(String.valueOf(innovationScore.getLb21()));
        innovationScore_Course.setText(String.valueOf(innovationScore.getLb22()));
        innovationScore_pratise.setText(String.valueOf(innovationScore.getLb3()));
        innovationScore_Lack.setText(String.valueOf(innovationScore.getLack()));
    }

    public void doBack(View view) {
        finish();
    }

    void setPageTheme(int id) {
        switch (id) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppTheme_Pink);
                break;
            case 2:
                setTheme(R.style.AppTheme_Red);
                break;
            case 3:
                setTheme(R.style.AppTheme_Orange);
                break;
            case 4:
                setTheme(R.style.AppTheme_Green);
                break;
        }
    }
}
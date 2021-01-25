package top.yvyan.guettable.moreFun;

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
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class InnovationScoreActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    @BindView(R.id.innovationScore_state)
    TextView innovationScoreState;
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
    @BindView(R.id.innovation_btn_getScore)
    ButtonView btn_get_innovationScore;
    @BindView(R.id.innovation_btn_update)
    ButtonView btn_update_innovationScore;

    private BaseResponse<InnovationScore> innovationScoreBaseResponse;
    private MoreFunService moreFunService;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innovation_score);
        ButterKnife.bind(this);
        moreFunService = new MoreFunService(this, this);
        moreFunService.update();
        btn_get_innovationScore.setOnClickListener(this);
        btn_update_innovationScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.innovationScore_back:
                finish();
                break;
            case R.id.innovation_btn_getScore:
                moreFunService.update();
                break;
            case R.id.innovation_btn_update:
                new Thread(() -> {
                    int result = StaticService.updateInnovationScore(this, cookie);
                    if (result == 0) {
                        ToastUtil.showToast(this, "更新数据成功!点击查询按钮查看积分");
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
        }
    }

    private void updateView() {
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
        ToastUtil.showToast(this, "获取成功!");
    }


}
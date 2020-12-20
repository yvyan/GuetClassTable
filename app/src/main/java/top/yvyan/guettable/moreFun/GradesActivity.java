package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;

public class GradesActivity extends AppCompatActivity implements IMoreFun {

    @BindView(R.id.grades_state)
    TextView grades_state;
    @BindView(R.id.grades_main)
    TextView gradesMain;
    @BindView(R.id.grades_year_1)
    TextView gradesYear1;
    @BindView(R.id.grades_year_2)
    TextView gradesYear2;
    @BindView(R.id.grades_year_3)
    TextView gradesYear3;
    @BindView(R.id.grades_year_4)
    TextView gradesYear4;
    @BindView(R.id.grades_year_5)
    TextView gradesYear5;
    @BindView(R.id.grades_year_6)
    TextView gradesYear6;

    GeneralData generalData;
    MoreDate moreDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        ButterKnife.bind(this);

        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void updateView() {
        float grades[] = moreDate.getGrades();
        gradesMain.setText(grades[0] + "");
        gradesYear1.setText(grades[1] + "");
        gradesYear2.setText(grades[2] + "");
        gradesYear3.setText(grades[3] + "");
        gradesYear4.setText(grades[4] + "");
        gradesYear5.setText(grades[5] + "");
        gradesYear6.setText(grades[6] + "");
    }

    public void onClick(View view) {
        finish();
    }

    @Override
    public int updateData(String cookie) {
        float grades[] = new float[]{0, 0, 0, 0, 0, 0, 0};
        grades[0] = StaticService.calculateGrades(this, cookie, null);
        if (grades[0] != -1) {
            int year = Integer.parseInt(generalData.getGrade());
            for (int i = 0; i < 6; i++) {
                grades[i + 1] = StaticService.calculateGrades(this, cookie, (year + i) + "-" + (year + i + 1));
            }
            moreDate.setGrades(grades);
            return 5;
        } else {
            return 1;
        }
    }

    @Override
    public void updateView(String hint, int state) {
        grades_state.setText(hint);
        if (state == 5) {
            updateView();
        }
    }
}
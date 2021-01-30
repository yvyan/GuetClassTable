package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

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
        DecimalFormat format = new DecimalFormat( "0.00");
        gradesMain.setText(format.format(grades[0]));
        gradesYear1.setText(format.format(grades[1]));
        gradesYear2.setText(format.format(grades[2]));
        gradesYear3.setText(format.format(grades[3]));
        gradesYear4.setText(format.format(grades[4]));
        gradesYear5.setText(format.format(grades[5]));
        gradesYear6.setText(format.format(grades[6]));
    }

    public void onClick(View view) {
        finish();
    }

    @Override
    public int updateData(String cookie) {
        float[] grades = StaticService.calculateGrades(this, cookie, Integer.parseInt(generalData.getGrade()));
        if (grades != null) {
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
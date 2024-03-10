package top.yvyan.guettable.moreFun;

import android.widget.TextView;

import java.text.DecimalFormat;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
import top.yvyan.guettable.util.AppUtil;

public class GradesActivity extends BaseFuncActivity {

    private GeneralData generalData;

    @Override
    protected void childInit() {
        setTitle(getResources().getString(R.string.moreFun_credits));
        setShowMore(false);
        AppUtil.reportFunc(getApplicationContext(), getString(R.string.moreFun_credits));

        generalData = GeneralData.newInstance(this);
    }

    @Override
    protected void showContent() {
        baseSetContentView(R.layout.activity_grades);
        TextView gradesMain = findViewById(R.id.grades_main);
        TextView gradesYear1 = findViewById(R.id.grades_year_1);
        TextView gradesYear2 = findViewById(R.id.grades_year_2);
        TextView gradesYear3 = findViewById(R.id.grades_year_3);
        TextView gradesYear4 = findViewById(R.id.grades_year_4);
        TextView gradesYear5 = findViewById(R.id.grades_year_5);
        TextView gradesYear6 = findViewById(R.id.grades_year_6);

        float[] grades = MoreData.getGrades();
        DecimalFormat format = new DecimalFormat("0.00");
        gradesMain.setText(format.format(grades[0]));
        gradesYear1.setText(format.format(grades[1]));
        gradesYear2.setText(format.format(grades[2]));
        gradesYear3.setText(format.format(grades[3]));
        gradesYear4.setText(format.format(grades[4]));
        gradesYear5.setText(format.format(grades[5]));
        gradesYear6.setText(format.format(grades[6]));
    }

    @Override
    public int updateData(TokenData tokenData) {
        float[] grades = StaticService.calculateGrades(this, tokenData.getBkjwCookie(), Integer.parseInt(generalData.getGrade()));
        if (grades != null) {
            MoreData.setGrades(grades);
            return 5;
        } else {
            return 1;
        }
    }
}

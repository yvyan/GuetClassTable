package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.R;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;

public class GradesActivity extends AppCompatActivity implements IMoreFun {

    @BindView(R.id.grades_state)
    TextView grades_state;
    @BindView(R.id.grades_not_find)
    TextView grades_not_find;

    float n = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        ButterKnife.bind(this);

        updateView();
        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    private void updateView() {
        grades_not_find.setText(n + "");
    }

    public void onClick(View view) {
        finish();
    }

    @Override
    public int updateData(String cookie) {
        n = StaticService.calculateGrades(this, cookie, null);
        if (n != -1) {
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
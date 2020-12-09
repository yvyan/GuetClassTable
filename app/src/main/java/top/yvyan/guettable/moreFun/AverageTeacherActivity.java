package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import top.yvyan.guettable.Gson.AvgTeacher;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;

public class AverageTeacherActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    private ImageView back;
    private TextView title;
    private RecyclerView recyclerView;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_teacher);

        back = findViewById(R.id.average_teacher_back);
        back.setOnClickListener(this);
        title = findViewById(R.id.average_teacher_state);
        recyclerView = findViewById(R.id.teacher_info_recycler_view);
        start = findViewById(R.id.avg_teacher_start);
        start.setOnClickListener(this);

        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    @Override
    public int updateData(String cookie) {
        List<AvgTeacher> avgTeachers = StaticService.getTeacherList(this, cookie, null);
        if (avgTeachers != null) {
            for (AvgTeacher avgTeacher : avgTeachers) {
                int n = StaticService.averageTeacher(this, cookie, avgTeacher, GeneralData.newInstance(this).getNumber());
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        title.setText(hint);
        if (state == 5) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.average_teacher_back:
                finish();
                break;
            default:
                break;
        }
    }
}
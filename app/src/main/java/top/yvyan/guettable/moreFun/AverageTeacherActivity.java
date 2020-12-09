package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.Gson.AvgTeacher;
import top.yvyan.guettable.Gson.AvgTeacherFormGet;
import top.yvyan.guettable.Gson.AvgTeacherFormSend;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.service.fetch.LAN;

public class AverageTeacherActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    private ImageView back;
    private TextView title;

    private TextView textView;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_teacher);

        back = findViewById(R.id.average_teacher_back);
        back.setOnClickListener(this);
        title = findViewById(R.id.average_teacher_state);

        textView = findViewById(R.id.test);

        MoreFunService moreFunService = new MoreFunService(this, this);
        moreFunService.update();
    }

    @Override
    public int updateData(String cookie) {
        List<AvgTeacher> avgTeachers = StaticService.getTeacherList(this, cookie, null);
        if (avgTeachers != null) {
            AvgTeacher avgTeacher = avgTeachers.get(8);
            Log.d("detailInfo", avgTeacher.toString());
            List<AvgTeacherFormGet> avgTeacherFormGets = StaticService.getAvgTeacherForm(this, cookie, avgTeacher);
            Log.d("detailInfo", avgTeacherFormGets.toString());

            List<AvgTeacherFormSend> avgTeacherFormSends = new ArrayList<>();
            for (AvgTeacherFormGet avgTeacherFormGet : avgTeacherFormGets) {
                avgTeacherFormSends.add(new AvgTeacherFormSend(avgTeacherFormGet, avgTeacher.getCourseid(), avgTeacher.getCourseno(), avgTeacher.getTeacherno(), avgTeacher.getTerm()));
            }
            Log.d("detailInfo", avgTeacherFormGets.toString());
            str = StaticService.saveTeacherForm(this, cookie, avgTeacherFormSends);
            if (str != null) {
                str = StaticService.commitTeacherForm(this, cookie, avgTeacherFormSends, GeneralData.newInstance(this).getNumber(), avgTeacher.getCname(), avgTeacher.getName(), avgTeacher.getTeacherno());
            }
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        title.setText(hint);
        if (state == 5) {
            textView.setText(str);
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
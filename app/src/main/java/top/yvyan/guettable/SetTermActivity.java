package top.yvyan.guettable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.button.ButtonView;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.AutoUpdate;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class SetTermActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private Button input;

    private String cookie;
    private GeneralData generalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_term);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
    }

    private void init() {
        Intent intent = getIntent();
        cookie = intent.getStringExtra("cookie");

        back = findViewById(R.id.back);
        input = findViewById(R.id.input);
        back.setOnClickListener(this);
        input.setOnClickListener(this);

        generalData = GeneralData.newInstance(this);

        new Thread(() -> {
            StudentInfo studentInfo = StaticService.getStudentInfo(this, cookie);
            generalData.setGrade(studentInfo.getGrade());
            generalData.setTerm(studentInfo.getTerm());
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.input:
                AutoUpdate.newInstance(this).firstLogin();
                ToastUtil.showLongToast(getApplicationContext(), "正在导入课表，受教务系统影响，需要约30秒，请耐心等待");
                finish();
                break;
        }
    }
}
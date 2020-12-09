package top.yvyan.guettable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.fragment.PersonFragment;
import top.yvyan.guettable.service.IMoreFun;
import top.yvyan.guettable.service.MoreFunService;
import top.yvyan.guettable.service.StaticService;
import top.yvyan.guettable.util.ToastUtil;

public class SetTermActivity extends AppCompatActivity implements View.OnClickListener, IMoreFun {

    private TextView stuId;
    private TextView stuName;
    private Spinner spinnerYear;
    private Spinner spinnerTerm;
    private Spinner spinnerWeek;
    private Button back;
    private Button input;

    private GeneralData generalData;
    private StudentInfo studentInfo;

    private boolean fromLogin = false;
    private String term;
    private String grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_term);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        init();

        if (generalData.getTerm() == null || fromLogin) {
            MoreFunService moreFunService = new MoreFunService(this, this);
            moreFunService.update();
        } else {
            initView();
        }
    }

    private void init() {
        //判断启动类
        Intent intent = getIntent();
        String path = intent.getStringExtra("fromLogin");
        if (path != null) {
            fromLogin = true;
        }

        stuId = findViewById(R.id.stu_id);
        stuName = findViewById(R.id.stu_name);
        spinnerYear = findViewById(R.id.spinner_year);
        spinnerTerm = findViewById(R.id.spinner_term);
        spinnerWeek = findViewById(R.id.spinner_weeks);
        back = findViewById(R.id.back);
        input = findViewById(R.id.input);
        back.setOnClickListener(this);
        input.setOnClickListener(this);

        generalData = GeneralData.newInstance(this);
    }

    private void initView() {
        grade = generalData.getGrade();
        term = generalData.getTerm();
        stuId.setText(generalData.getNumber());
        stuName.setText(generalData.getName());
        int num = Integer.parseInt(grade);
        setYearSpinner(num);
        //自动选择年度
        int nowYear = Integer.parseInt(term.substring(0, 4));
        nowYear = nowYear - num;
        spinnerYear.setSelection(nowYear);
        //自动选择学期
        int nowTerm = Integer.parseInt(term.substring(10, 11));
        spinnerTerm.setSelection(nowTerm - 1);
        //自动选择星期
        int week = generalData.getWeek();
        spinnerWeek.setSelection(week - 1);
    }

    private void setYearSpinner(int num) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        adapter.add(num + "-" + (num + 1) + "(大一)");
        num++;
        adapter.add(num + "-" + (num + 1) + "(大二)");
        num++;
        adapter.add(num + "-" + (num + 1) + "(大三)");
        num++;
        adapter.add(num + "-" + (num + 1) + "(大四)");
        num++;
        adapter.add(num + "-" + (num + 1) + "(大五)");
        num++;
        adapter.add(num + "-" + (num + 1) + "(大六)");
        num++;
        spinnerYear.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AccountData.newInstance(this).logoff();
                PersonFragment.newInstance().updateView();
                finish();
                break;
            case R.id.input:
                //保存学年
                int year = Integer.parseInt(generalData.getGrade()) + (int)spinnerYear.getSelectedItemId();
                int num = (int)spinnerTerm.getSelectedItemId() + 1;
                generalData.setTerm(year + "-" + (year + 1) + "_" + num);
                //保存星期
                int week = (int)spinnerWeek.getSelectedItemId() + 1;
                generalData.setWeek(week);

                GeneralData.newInstance(this).setLastUpdateTime(-1);
                PersonFragment personFragment = PersonFragment.newInstance();
                personFragment.updateView();
                personFragment.getOnButtonClick().onClick(0); //切换页面0

                ToastUtil.showLongToast(getApplicationContext(), "正在导入课表，受教务系统影响，最长需要约30秒，请耐心等待");
                finish();
                break;
        }
    }

    @Override
    public int updateData(String cookie) {
        studentInfo = StaticService.getStudentInfo(this, cookie);
        if (studentInfo != null) {
            generalData.setNumber(studentInfo.getStid());
            generalData.setName(studentInfo.getName());
            generalData.setTerm(studentInfo.getTerm());
            generalData.setGrade(studentInfo.getGrade());
            return 5;
        }
        return 1;
    }

    @Override
    public void updateView(String hint, int state) {
        if (state == 5) {
            initView();
        }
    }
}
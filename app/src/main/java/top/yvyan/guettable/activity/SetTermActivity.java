package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.umcrash.UMCrash;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.picker.XSeekBar;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;

public class SetTermActivity extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE = 14;
    public static int OK = 10;
    public static int OFF = 11;

    private TextView stuId;
    private TextView stuName;
    private TextView week_text;
    private Spinner spinnerYear;
    private Spinner spinnerTerm;
    private XSeekBar seekBar;

    private GeneralData generalData;
    private ScheduleData scheduleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_term);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (!AccountData.newInstance(this).getIsLogin()) {
            ToastUtil.showToast(getApplicationContext(), "您还未登录！");
            finish();
        } else {
            init();
            initView();
        }
    }

    private void init() {
        seekBar = findViewById(R.id.seekBar_week);
        stuId = findViewById(R.id.stu_id);
        stuName = findViewById(R.id.stu_name);
        spinnerYear = findViewById(R.id.spinner_year);
        spinnerTerm = findViewById(R.id.spinner_term);
        ButtonView back = findViewById(R.id.logoff);
        ButtonView input = findViewById(R.id.input);
        week_text = findViewById(R.id.week_text);
        back.setOnClickListener(this);
        input.setOnClickListener(this);
        generalData = GeneralData.newInstance(this);
        scheduleData = ScheduleData.newInstance(this);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        String grade = generalData.getGrade();
        String term = generalData.getTerm();
        stuId.setText(generalData.getNumber());
        stuName.setText(generalData.getName());
        int num = 2018;
        try {
            num = Integer.parseInt(grade);
        } catch (Exception e) {
            UMCrash.generateCustomLog(e, "SetTerm.initView.Integer.parseInt");
        }
        setYearSpinner(num);
        //自动选择年度
        int nowYear = Integer.parseInt(term.substring(0, 4));
        nowYear = nowYear - num;
        spinnerYear.setSelection(nowYear);
        //自动选择学期
        int nowTerm;
        nowTerm = Integer.parseInt(term.substring(10, 11));
        spinnerTerm.setSelection(nowTerm - 1);
        //自动选择星期
        int week = generalData.getWeek();
        week_text.setText("第" + week + "周");
        seekBar.setMax(generalData.getMaxWeek() * 10);
        seekBar.setDefaultValue(week * 10);
        seekBar.setOnSeekBarListener((seekBar, newValue) -> week_text.setText("第" + (newValue / 10) + "周")
        );
    }

    private void setYearSpinner(int num) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
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
        spinnerYear.setAdapter(adapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoff:
                AccountData.newInstance(this).logoff();
                Intent intent = getIntent();
                setResult(OFF, intent);
                finish();
                break;
            case R.id.input:
                //保存学年
                ScheduleData scheduleData = ScheduleData.newInstance(getApplicationContext());
                int year = Integer.parseInt(generalData.getGrade()) + (int) spinnerYear.getSelectedItemId();
                int num = (int) spinnerTerm.getSelectedItemId() + 1;
                String term_1 = year + "-" + (year + 1) + "_" + num;
                for (TermBean termBean : MoreDate.newInstance(this).getTermBeans()) {
                    String termString = termBean.getTerm();
                    if (termString != null
                            && termString.length() >= 11
                            && termString.substring(0, 4).equals(String.valueOf(year))
                            && termString.substring(10, 11).equals(String.valueOf(num))) {
                        term_1 = termString;
                        break;
                    }
                }
                generalData.setTerm(term_1);
                scheduleData.deleteInputCourse();
                if (scheduleData.getUserCourseBeans().size() != 0) {
                    DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                        @Override
                        public void onClickYes() {
                            importCourse();
                        }

                        @Override
                        public void onClickBack() {
                            scheduleData.deleteUserCourse();
                            importCourse();
                        }
                    };
                    DialogUtil.showDialog(this, "提示", false, "保留", "删除", "您修改了学期/账号，是否保留手动添加的课程？\r\n\r\nTip:若您只是临时切换，建议保留。", service);
                } else {
                    importCourse();
                }
                break;
        }
    }

    private void importCourse() {
        //保存星期
        int week = seekBar.getSelectedNumber() / 10;
        generalData.setWeek(week);
        generalData.setLastUpdateTime(-1);
        scheduleData.setUpdate(true);
        ToastUtil.showToast(getApplicationContext(), "正在导入课表，受教务系统影响，最长需要约10秒，请耐心等待，不要滑动页面");
        Intent intent = getIntent();
        setResult(OK, intent);
        finish();
    }

}
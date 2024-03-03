package top.yvyan.guettable.activity;

import static com.xuexiang.xui.XUI.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.umeng.cconfig.UMRemoteConfig;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.picker.XSeekBar;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.Gson.CurrentSemester;
import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.fetch.StaticService;
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
    private ButtonView input;
    private CheckBox cb_addTerm;

    private GeneralData generalData;

    private MiniLoadingDialog mMiniLoadingDialog;

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
        Intent thisIntent = getIntent();
        //自动设置学期&星期
        boolean auto = thisIntent.getBooleanExtra("auto", false);
        mMiniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this);
        if (auto) {
            //存储日期模式为自动
            GeneralData.setAutoTerm(true);
            //请求/解析数据
            getDate();
        }
    }

    private void init() {
        seekBar = findViewById(R.id.seekBar_week);
        stuId = findViewById(R.id.stu_id);
        stuName = findViewById(R.id.stu_name);
        spinnerYear = findViewById(R.id.spinner_year);
        spinnerTerm = findViewById(R.id.spinner_term);
        cb_addTerm = findViewById(R.id.cb_addTerm);
        cb_addTerm.setChecked(false);
        //显示&隐藏“关联小学期”
        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                View show = findViewById(R.id.show_addTerm);
                View place = findViewById(R.id.show_addTerm_place);
                if (false) { //第二学期
                    show.setVisibility(View.VISIBLE);
                    place.setVisibility(View.GONE);
                } else {
                    show.setVisibility(View.GONE);
                    place.setVisibility(View.VISIBLE);
                    cb_addTerm.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ButtonView back = findViewById(R.id.logoff);
        input = findViewById(R.id.input);
        week_text = findViewById(R.id.week_text);
        back.setOnClickListener(this);
        input.setOnClickListener(this);
        generalData = GeneralData.newInstance(this);
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
        } catch (Exception ignored) {
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

    /**
     * 因为教务有时学期格式不标准，所以将学期替换为教务返回的数据
     *
     * @param year         学年
     * @param num          学期
     * @param termBeanList 教务返回的学期列表
     * @return 格式化后的学期数据
     */
    private String formatTerm(int year, int num, List<TermBean> termBeanList) {
        String term = year + "-" + (year + 1) + "_" + num;
        for (TermBean termBean : termBeanList) {
            String termString = termBean.getTerm();
            if (termString != null
                    && termString.length() >= 11
                    && termString.substring(0, 4).equals(String.valueOf(year))
                    && termString.substring(10, 11).equals(String.valueOf(num))) {
                term = termString;
                break;
            }
        }
        return term;
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
                int year = Integer.parseInt(generalData.getGrade()) + (int) spinnerYear.getSelectedItemId();
                int num = (int) spinnerTerm.getSelectedItemId() + 1;
                String term_1 = formatTerm(year, num, MoreData.getTermBeans());
                if (num == 2 && cb_addTerm.isChecked()) { //关联小学期
                    String term_2 = formatTerm(year, num + 1, MoreData.getTermBeans());
                    generalData.setAddTerm(term_2);
                } else {
                    generalData.setAddTerm("");
                }
                generalData.setTerm(term_1);
                ScheduleData.deleteInputCourse();
                if (ScheduleData.getUserCourseBeans().size() != 0) {
                    DialogUtil.IDialogService service = new DialogUtil.IDialogService() {
                        @Override
                        public void onClickYes() {
                            importCourse();
                        }

                        @Override
                        public void onClickBack() {
                            ScheduleData.deleteUserCourse();
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
        GeneralData.setAutoTerm(false); //关闭自动学期
        ScheduleData.setUpdate(true);
        ToastUtil.showToast(getApplicationContext(), "正在导入课表，受教务系统影响，最长需要约10秒，请耐心等待，不要滑动页面");
        Intent intent = getIntent();
        setResult(OK, intent);
        finish();
    }

    /**
     * 在线获取学期信息
     */
    private void getDate() {
        new Thread(() -> {
            //显示loading
            runOnUiThread(() -> {
                input.setClickable(false);
                mMiniLoadingDialog.updateMessage("正在设置学期...");
                mMiniLoadingDialog.show();
            });
            //设置
            try {
                int n = setDate();
                if (n == 0) { //成功
                    runOnUiThread(() -> {
                        input.setClickable(true);
                        mMiniLoadingDialog.dismiss();
                        ToastUtil.showToast(getApplicationContext(), "正在导入课表，受教务系统影响，最长需要约10秒，请耐心等待，不要滑动页面");
                    });
                    //删除已有课程，重新导入
                    generalData.setLastUpdateTime(-1);
                    ScheduleData.deleteInputCourse();
                    ScheduleData.setUpdate(true);
                    Intent intent = getIntent();
                    setResult(OK, intent);
                    finish();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                input.setClickable(true);
                mMiniLoadingDialog.dismiss();
            });
        }).start();
    }

    /**
     * 独立出来的请求和解析学期的函数（需要在线程内执行）
     *
     * @return 0  成功
     * -1 失败
     */
    private int setDate() {
        TokenData tokenData = TokenData.newInstance(this);
        CurrentSemester semester = StaticService.getSemester(this, tokenData.getbkjwTestCookie());
        if (semester == null) return -1;
        generalData.setTerm(semester.toString());
        generalData.setStartTime(semester.startDate.getTime());
        generalData.setEndTime(semester.endDate.getTime());
        return 0;
    }


    /**
     * 关联小学期的提示
     */
    public void showHelp(View view) {
        DialogUtil.showTextDialog(this, getContext().getResources().getString(R.string.addTermHelp));
    }
}
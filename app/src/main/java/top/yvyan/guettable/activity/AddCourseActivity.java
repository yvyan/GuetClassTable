package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.ScheduleData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.TimeUtil;
import top.yvyan.guettable.util.ToastUtil;

public class AddCourseActivity extends AppCompatActivity {

    GeneralData generalData;

    SeekBar weekStartSeekBar;
    SeekBar weekEndSeekBar;
    TextView weekStartTextView;
    TextView weekEndTextView;
    SeekBar daySeekBar;
    TextView dayTextView;
    SeekBar courseStartSeekBar;
    TextView courseStartTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        TextView title = findViewById(R.id.title);
        title.setText("添加课程");
        //透明状态栏
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View addStatus = findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getApplicationContext()));
        addStatus.setLayoutParams(lp);
        //自定义背景图
        ImageView background = findViewById(R.id.background);
        View titleBar = findViewById(R.id.func_base_constraintLayout);
        if (BackgroundUtil.isSetBackground(this)) {
            BackgroundUtil.setBackground(this, background);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryTransparent));
        } else {
            background.setImageBitmap(null);
            addStatus.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        //数据初始化
        generalData = GeneralData.newInstance(getApplicationContext());
        Intent thisIntent = getIntent();
        int week = thisIntent.getIntExtra("week", 1);
        int day = thisIntent.getIntExtra("day", 0);
        int start = thisIntent.getIntExtra("start", 1);
        initTimeSeekBar(week, day, start);
    }

    /**
     * SeekBar初始化
     *
     * @param week  第几周
     * @param day   周几
     * @param start 第几大节
     */
    @SuppressLint("SetTextI18n")
    private void initTimeSeekBar(int week, int day, int start) {
        weekStartSeekBar = findViewById(R.id.seekBar_week_start);
        weekEndSeekBar = findViewById(R.id.seekBar_week_end);
        weekStartTextView = findViewById(R.id.textView_week_start);
        weekEndTextView = findViewById(R.id.textView_week_end);
        weekStartSeekBar.setMax(generalData.getMaxWeek() - 1);
        weekStartSeekBar.setProgress(week - 1);
        weekStartSeekBar.setOnSeekBarChangeListener(weekSeekBarListener);
        weekStartTextView.setText(String.valueOf(week));
        weekEndSeekBar.setMax(generalData.getMaxWeek() - 1);
        weekEndSeekBar.setProgress(week - 1);
        weekEndSeekBar.setOnSeekBarChangeListener(weekSeekBarListener);
        weekEndTextView.setText(String.valueOf(week));

        daySeekBar = findViewById(R.id.seekBar_day);
        dayTextView = findViewById(R.id.textView_day);
        daySeekBar.setProgress(day - 1);
        daySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                dayTextView.setText(TimeUtil.whichDay(seekBar.getProgress() + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dayTextView.setText(TimeUtil.whichDay(day));

        courseStartSeekBar = findViewById(R.id.seekBar_course_start);
        courseStartTextView = findViewById(R.id.textView_course_start);
        courseStartSeekBar.setProgress(start);
        courseStartSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                courseStartTextView.setText(seekBar.getProgress() + "大节");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        courseStartTextView.setText(start + "大节");
    }

    /**
     * 周次选择控件监听
     */
    private final SeekBar.OnSeekBarChangeListener weekSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (R.id.seekBar_week_start == seekBar.getId()) { //开始周
                if (seekBar.getProgress() > weekEndSeekBar.getProgress()) {
                    weekEndSeekBar.setProgress(seekBar.getProgress());
                }
            } else { //结束周
                if (seekBar.getProgress() < weekStartSeekBar.getProgress()) {
                    weekStartSeekBar.setProgress(seekBar.getProgress());
                }
            }
            weekStartTextView.setText(String.valueOf(weekStartSeekBar.getProgress() + 1));
            weekEndTextView.setText(String.valueOf(weekEndSeekBar.getProgress() + 1));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public void buttonAdd(View view) {
        EditText courseNameEditText = findViewById(R.id.add_course_name);
        EditText courseNumberEditText = findViewById(R.id.add_course_number);
        EditText courseTeacherEditText = findViewById(R.id.add_course_teacher);
        EditText coursePlaceEditText = findViewById(R.id.add_course_place);
        EditText courseCommEditText = findViewById(R.id.add_course_comm);
        String courseName = courseNameEditText.getText().toString();
        if (courseName.isEmpty()) {
            ToastUtil.showToast(getApplicationContext(), "课程名称不能为空！");
        } else {
            ScheduleData scheduleData = ScheduleData.newInstance(getApplicationContext());
            List<CourseBean> courseBeans = scheduleData.getUserCourseBeans();
            CourseBean courseBean = new CourseBean();
            courseBean.setCourse(
                    (courseNumberEditText.getText().toString().isEmpty() ? null : courseNumberEditText.getText().toString()),
                    courseName,
                    (coursePlaceEditText.getText().toString().isEmpty() ? null : coursePlaceEditText.getText().toString()),
                    weekStartSeekBar.getProgress() + 1,
                    weekEndSeekBar.getProgress() + 1,
                    daySeekBar.getProgress() + 1,
                    courseStartSeekBar.getProgress(),
                    (courseTeacherEditText.getText().toString().isEmpty() ? null : courseTeacherEditText.getText().toString()),
                    (courseCommEditText.getText().toString().isEmpty() ? null : courseCommEditText.getText().toString())
            );
            try {
                courseBeans.add(courseBean);
            } catch (Exception e) {
                e.printStackTrace();
            }

            scheduleData.setUserCourseBeans(courseBeans);
            ToastUtil.showToast(getApplicationContext(), "添加成功！");
        }
    }

    public void doBack(View view) {
        finish();
    }
}
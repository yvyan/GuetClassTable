package top.yvyan.guettable.activity;

import static java.lang.Math.max;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.data.DetailClassData;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.BackgroundUtil;
import top.yvyan.guettable.util.CourseUtil;
import top.yvyan.guettable.util.TimeUtil;

public class DetailActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 11;
    public static int ALTER = 10;

    List<Schedule> schedules;
    int week;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingleSettingData singleSettingData = SingleSettingData.newInstance(getApplicationContext());
        BackgroundUtil.setPageTheme(this, singleSettingData.getThemeId());
        setContentView(R.layout.activity_detail);
        schedules = DetailClassData.getCourseBeans();
        //透明状态栏
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View addStatus = findViewById(R.id.add_status);
        ViewGroup.LayoutParams lp = addStatus.getLayoutParams();
        lp.height = lp.height + AppUtil.getStatusBarHeight(Objects.requireNonNull(getApplicationContext()));
        addStatus.setLayoutParams(lp);
        //加载时间信息
        Intent thisIntent = getIntent();
        week = (Integer) Objects.requireNonNull(thisIntent.getExtras()).get("week");
        TextView detailTitle = findViewById(R.id.title);
        StringBuilder section = new StringBuilder();
        int day = schedules.get(0).getDay();
        int start = schedules.get(0).getStart();
        int end = schedules.get(0).getStep() + start - 1;
        for (int i = start; i <= end; i++) {
            if (i == 5) {
                section.append("中午, ");
            }
            if (i % 2 == 0) {
                section.append((i + (i < 5 ? 1 : 0)) / 2 + ", ");
            }
        }
        detailTitle.setText("第" + week + "周" + TimeUtil.whichDay(day) + " 第" + section.toString().substring(0,  max(0,section.length() - 2)) + "大节");
        //添加课程控件初始化
        ImageView add = findViewById(R.id.more);
        add.setImageDrawable(getResources().getDrawable(R.drawable.d_add));
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
            intent.putExtra("week", week);
            intent.putExtra("day", day);
            intent.putExtra("start", (start == 5 ? 3 : (start > 5 ? start+2 : start+1)/2));
            startActivityForResult(intent, AddCourseActivity.REQUEST_CODE);
        });
        //自定义背景图
        ImageView background = findViewById(R.id.background);
        View titleBar = findViewById(R.id.func_base_constraintLayout);
        if (BackgroundUtil.isSetBackground(this)) {
            BackgroundUtil.setBackground(this, background);
            titleBar.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
            addStatus.getBackground().setAlpha((int) singleSettingData.getTitleBarAlpha());
        } else {
            background.setImageBitmap(null);
            titleBar.getBackground().setAlpha(255);
            addStatus.getBackground().setAlpha(255);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData(schedules, week);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddCourseActivity.REQUEST_CODE && resultCode == AddCourseActivity.ADD) {
            Intent intent = getIntent();
            setResult(ALTER, intent);
            finish();
        }
    }

    private void loadData(List<Schedule> schedules, int week) {
        //加载课程信息
        CourseUtil.ComparatorCourse comparatorCourse = new CourseUtil.ComparatorCourse();
        Collections.sort(schedules, comparatorCourse);
        RecyclerView recyclerView = findViewById(R.id.class_detail_recycleView);
        ClassDetailAdapter classDetailAdapter = new ClassDetailAdapter(getIntent(), this, schedules, week);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classDetailAdapter);
    }

    public void doBack(View view) {
        finish();
    }
}
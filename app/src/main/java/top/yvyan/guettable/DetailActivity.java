package top.yvyan.guettable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.data.DetailClassData;
import top.yvyan.guettable.util.ComparatorCourse;
import top.yvyan.guettable.util.TimeUtil;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        List<Schedule> schedules = DetailClassData.newInstance().getCourseBeans();

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener((view) -> finish());

        Intent intent = getIntent();
        int week = (Integer) intent.getExtras().get("week");
        TextView detailTitle = findViewById(R.id.title);
        int n = (schedules.get(0).getStart() / 2 + 1);
        if (n == 7) {
            n = 0;
        }
        detailTitle.setText("第" + week + "周" + TimeUtil.whichDay(schedules.get(0).getDay()) + " 第" + n + "大节");

        ComparatorCourse comparatorCourse = new ComparatorCourse();
        Collections.sort(schedules, comparatorCourse);
        RecyclerView recyclerView = findViewById(R.id.class_detail_recycleView);
        ClassDetailAdapter classDetailAdapter = new ClassDetailAdapter(schedules, week);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classDetailAdapter);
    }
}
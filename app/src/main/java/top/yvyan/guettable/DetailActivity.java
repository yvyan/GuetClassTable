package top.yvyan.guettable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhuangfei.timetable.model.Schedule;

import java.util.Collections;
import java.util.List;

import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.DetailClassData;
import top.yvyan.guettable.util.ComparatorCourse;

public class DetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ClassDetailAdapter classDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        List<Schedule> schedules = DetailClassData.newInstance().getCourseBeans();
        ComparatorCourse comparatorCourse = new ComparatorCourse();
        Collections.sort(schedules, comparatorCourse);
        recyclerView = findViewById(R.id.class_detail_recycleView);
        classDetailAdapter = new ClassDetailAdapter(schedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classDetailAdapter);
    }
}
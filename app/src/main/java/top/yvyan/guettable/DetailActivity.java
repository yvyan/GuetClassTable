package top.yvyan.guettable;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import top.yvyan.guettable.adapter.ClassDetailAdapter;
import top.yvyan.guettable.data.DayClassData;

public class DetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ClassDetailAdapter classDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        recyclerView = findViewById(R.id.class_detail_recycleView);
        classDetailAdapter = new ClassDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classDetailAdapter);
    }
}
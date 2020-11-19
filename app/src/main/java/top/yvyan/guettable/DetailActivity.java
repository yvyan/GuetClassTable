package top.yvyan.guettable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import top.yvyan.guettable.adapter.ClassDetailAdapter;

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
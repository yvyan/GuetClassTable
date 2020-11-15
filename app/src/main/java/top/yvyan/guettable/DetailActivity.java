package top.yvyan.guettable;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import top.yvyan.guettable.data.DayClassData;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DayClassData dayClassData = DayClassData.newInstance();
        TextView detail = findViewById(R.id.detail);
        detail.setText(dayClassData.getCourseBeans().toString());
    }
}
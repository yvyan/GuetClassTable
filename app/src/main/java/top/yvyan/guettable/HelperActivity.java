package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import top.yvyan.guettable.util.AppUtil;

public class HelperActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.person_help));
        ImageView back = findViewById(R.id.back);
        Button join = findViewById(R.id.btn_join2);
        back.setOnClickListener(this);
        join.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_join2:
                AppUtil.addQQ(this);
                finish();
        }
    }
}
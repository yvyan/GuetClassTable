package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import top.yvyan.guettable.util.AppUtil;

public class HelperActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        back = findViewById(R.id.helper_back);
        join = findViewById(R.id.btn_join2);
        back.setOnClickListener(this);
        join.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.helper_back:
                finish();
                break;
            case R.id.btn_join2:
                AppUtil.addQQ(this);
                finish();
        }
    }
}
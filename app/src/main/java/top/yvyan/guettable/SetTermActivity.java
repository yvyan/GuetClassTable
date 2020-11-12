package top.yvyan.guettable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import top.yvyan.guettable.service.GetDataService;

public class SetTermActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private Button input;

    private String cookie;

    public SetTermActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_term);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        back = findViewById(R.id.back);
        input = findViewById(R.id.input);
        back.setOnClickListener(this);
        input.setOnClickListener(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        cookie = intent.getStringExtra("cookie");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.input:
                GetDataService.getClassTable(this, cookie, "2020-2021_1");
                finish();
                break;
        }
    }
}
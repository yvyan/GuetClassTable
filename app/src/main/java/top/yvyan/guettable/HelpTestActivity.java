package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.util.ToastUtil;

public class HelpTestActivity extends AppCompatActivity {

    @BindView(R.id.develop_state)
    TextView state;
    @BindView(R.id.develop_input)
    EditText input;

    TokenData tokenData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_test);
        ButterKnife.bind(this);

        tokenData = TokenData.newInstance(getApplicationContext());
        updateView();
    }

    /**
     * 更新状态
     */
    private void updateView() {
        if (tokenData.isDevelop()) {
            state.setText("开启");
        } else {
            state.setText("关闭");
        }
    }

    public void doBack(View view) {
        finish();
    }

    public void developStart(View view) {
        String token = input.getText().toString();
        if (token.equals("")) {
            ToastUtil.showToast(getApplicationContext(), "内容不能为空！");
        } else {
            tokenData.setVPNToken(token);
            tokenData.setDevelop(true);
            updateView();
        }
    }

    public void developClose(View view) {
        tokenData.setVPNToken("");
        tokenData.setDevelop(false);
        updateView();
    }
}
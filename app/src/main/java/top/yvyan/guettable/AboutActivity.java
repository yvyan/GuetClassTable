package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        back = findViewById(R.id.about_back);
        back.setOnClickListener(this::onClick);
        join = findViewById(R.id.btn_join);
        join.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_back:
                finish();
                break;
            case R.id.btn_join:
                join();
                finish();
        }
    }

    private boolean join() {
        String key = "b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP";
        return joinQQGroup(key);
    }

    /****************
     *
     * 发起添加群流程。群号：桂电课程表交流群(963908505) 的 key 为： b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP
     * 调用 joinQQGroup(b6B5JNbpsHV0scWnS1amN7NH3Ry-LlrP) 即可发起手Q客户端申请加群 桂电课程表交流群(963908505)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    private boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
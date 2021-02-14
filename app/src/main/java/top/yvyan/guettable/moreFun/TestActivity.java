package top.yvyan.guettable.moreFun;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.umeng.cconfig.UMRemoteConfig;

import top.yvyan.guettable.R;
import top.yvyan.guettable.util.TextDialog;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String result = UMRemoteConfig.getInstance().getConfigValue("pushInfo");
        if (result != null) {
            TextDialog.showScanNumberDialog(this, result);
        }
    }
}
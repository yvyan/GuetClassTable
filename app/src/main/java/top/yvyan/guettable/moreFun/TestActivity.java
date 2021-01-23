package top.yvyan.guettable.moreFun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import top.yvyan.guettable.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        boolean result = isInternational("1861150201");
        Log.d("1586", result + "");
        result = isInternational("xx611xxxxx");
        Log.d("1586", result + "");
        result = isInternational("xx611xxxx");
        Log.d("1586", result + "");
    }

    private boolean isInternational(String account) {
        if (account.isEmpty()) {
            return false;
        } else return account.length() == 10 && account.startsWith("611", 2);
    }
}
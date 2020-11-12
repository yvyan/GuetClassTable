package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.LoginActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private static DayClassFragment dayClassFragment;

    private View view;
    private TextView textView;
    private Button goToLogin;
    private AccountData accountData;

    public DayClassFragment() {
        // Required empty public constructor
    }

    public static DayClassFragment newInstance() {
        if (dayClassFragment == null) {
            DayClassFragment fragment = new DayClassFragment();
            dayClassFragment = fragment;
        }
        return dayClassFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_class, container, false);

        textView = view.findViewById(R.id.day_class_test);
        goToLogin = view.findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(this);
        accountData = AccountData.newInstance(getActivity());
        updateUser();
        Log.d("test:", "create");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("test:", "start");
        //updateUser();
    }

    @Override
    public void onClick(View view) {
        if (accountData.getIsLogin()) {
            accountData.logoff();
            updateUser();
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void updateUser() {
        if (accountData.getIsLogin()) {
            textView.setText("已登录");
            goToLogin.setText("退出");
        } else {
            textView.setText("未登录");
            goToLogin.setText("登陆");
        }
    }

    public void updateText(String text) {
        textView.setText(text);
        Log.d("test:", "updateText");
    }
}
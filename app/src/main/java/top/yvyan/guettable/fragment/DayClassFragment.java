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
import top.yvyan.guettable.data.UserData;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView textView;
    private Button goToLogin;
    private UserData userData;

    public DayClassFragment() {
        // Required empty public constructor
    }

    public static DayClassFragment newInstance() {
        DayClassFragment fragment = new DayClassFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_class, container, false);

        textView = view.findViewById(R.id.day_class_test);
        goToLogin = view.findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(this);
        userData = UserData.newInstance(getActivity());
        updateUser();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("test:", "start");
        updateUser();
    }

    @Override
    public void onClick(View view) {
        if (userData.getIsLogin()) {
            userData.logoff();
            updateUser();
        } else {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void updateUser() {
        if (userData.getIsLogin()) {
            textView.setText("已登录");
            goToLogin.setText("退出");
        } else {
            textView.setText("未登录");
            goToLogin.setText("登陆");
        }
    }
}
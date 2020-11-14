package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.LoginActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.service.GetDataService;

public class DayClassFragment extends Fragment implements View.OnClickListener {

    private static DayClassFragment dayClassFragment;

    private View view;
    private TextView textView;
    private AccountData accountData;
    private GeneralData generalData;

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
        textView.setOnClickListener(this);
        accountData = AccountData.newInstance(getActivity());
        generalData = GeneralData.newInstance(getActivity());
        updateUser();
        if (accountData.getIsLogin()) {
            GetDataService.autoUpdateThread(getActivity(), accountData.getUsername(), accountData.getPassword(), generalData.getTerm());
        }
        Log.d("test:", "create");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("test:", "start");
        //updateUser();
    }

    /**
     * 用户点击状态文字时的响应
     * @param view 视图
     */
    @Override
    public void onClick(View view) {
        if ("请登录".equals(textView.getText()) || "密码错误".equals(textView.getText())) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        } else if ("更新成功".equals(textView.getText()) || "已登录".equals(textView.getText()) || "网络错误".equals(textView.getText())) {
            //启动自动更新线程
            if (accountData.getIsLogin()) {
                GetDataService.autoUpdateThread(getActivity(), accountData.getUsername(), accountData.getPassword(), generalData.getTerm());
            }
        }
    }

    private void updateUser() {
        if (accountData.getIsLogin()) {
            textView.setText("已登录");
        } else {
            textView.setText("请登录");
        }
    }

    public void updateText(String text) {
        textView.setText(text);
        Log.d("test:", "updateText");
    }
}
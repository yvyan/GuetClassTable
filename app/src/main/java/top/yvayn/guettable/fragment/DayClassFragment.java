package top.yvayn.guettable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import top.yvayn.guettable.R;

public class DayClassFragment extends Fragment {

    private View view;

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
        TextView textView = view.findViewById(R.id.day_class_test);
        textView.setText("测试显示！");
        return view;
    }
}
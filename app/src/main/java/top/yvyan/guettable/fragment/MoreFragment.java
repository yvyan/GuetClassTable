package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.ExamActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.util.ToastUtil;

public class MoreFragment extends Fragment implements View.OnClickListener {
    private static MoreFragment moreFragment;

    private View view;
    private View more_item_1, more_item_2, more_item_3;

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance() {
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }
        return moreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_more, container, false);

        more_item_1 = view.findViewById(R.id.more_item_1);
        more_item_2 = view.findViewById(R.id.more_item_2);
        more_item_3 = view.findViewById(R.id.more_item_3);
        more_item_1.setOnClickListener(this);
        more_item_2.setOnClickListener(this);
        more_item_3.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_item_1:
                Intent intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            default:
                ToastUtil.showToast(getContext(), "敬请期待！");
        }
    }
}

package top.yvyan.guettable.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.WebViewActivity;
import top.yvyan.guettable.moreFun.AverageTeacherActivity;
import top.yvyan.guettable.moreFun.CETActivity;
import top.yvyan.guettable.moreFun.ExamActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.moreFun.ExamScoreActivity;
import top.yvyan.guettable.moreFun.ExperimentScoreActivity;
import top.yvyan.guettable.util.ToastUtil;

public class MoreFragment extends Fragment implements View.OnClickListener {
    private static MoreFragment moreFragment;

    private View view;
    private View more_item_1, more_item_2, more_item_3, more_item_4, more_item_5, more_item_6, more_index;

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
        more_item_4 = view.findViewById(R.id.more_item_4);
        more_item_5 = view.findViewById(R.id.more_item_5);
        more_item_6 = view.findViewById(R.id.more_item_6);
        more_index = view.findViewById(R.id.more_index);
        more_item_1.setOnClickListener(this);
        more_item_2.setOnClickListener(this);
        more_item_3.setOnClickListener(this);
        more_item_4.setOnClickListener(this);
        more_item_5.setOnClickListener(this);
        more_item_6.setOnClickListener(this);
        more_index.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.more_item_1:
                intent = new Intent(getContext(), ExamActivity.class);
                startActivity(intent);
                break;
            case R.id.more_item_2:
                intent = new Intent(getContext(), CETActivity.class);
                startActivity(intent);
                break;
            case R.id.more_item_3:
                intent = new Intent(getContext(), ExamScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.more_item_4:
                intent = new Intent(getContext(), ExperimentScoreActivity.class);
                startActivity(intent);
                break;
            case R.id.more_item_5:
                intent = new Intent(getContext(), AverageTeacherActivity.class);
                startActivity(intent);
                break;
            case R.id.more_item_6:
                ToastUtil.showToast(getContext(), "下版本更新，敬请期待！");
                break;
            case R.id.more_index:
                Uri uri = Uri.parse(getContext().getResources().getString(R.string.guet_yvyan_top));
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(uri);
                startActivity(intent);
                break;
            default:
                ToastUtil.showToast(getContext(), "敬请期待！");
        }
    }
}

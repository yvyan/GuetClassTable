package top.yvyan.guettable.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.R;

public class MoreFragment extends Fragment {
    private static MoreFragment moreFragment;

    private View view;
    private TextView textView;

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

        textView = view.findViewById(R.id.more_hint);
        return view;
    }
}

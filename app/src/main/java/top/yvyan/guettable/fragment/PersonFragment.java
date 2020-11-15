package top.yvyan.guettable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import top.yvyan.guettable.LoginActivity;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.AccountData;

public class PersonFragment extends Fragment implements View.OnClickListener {
    private static PersonFragment personFragment;

    private static final String TAG = "PersonFragment";
    private View view;
    private View afterLogin;
    private View beforeLogin;
    private Button buttonQuit;

    private AccountData accountData;

    public PersonFragment() {
    }

    public static PersonFragment newInstance() {
        if (personFragment == null) {
            personFragment = new PersonFragment();
        }
        return personFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "createPersonFragmentView");
        view = inflater.inflate(R.layout.fragement_preson, container, false);
        accountData = AccountData.newInstance(getActivity());
        afterLogin = view.findViewById(R.id.afterLogin);
        beforeLogin = view.findViewById(R.id.before_login);
        beforeLogin.setOnClickListener(this);
        buttonQuit = view.findViewById(R.id.btn_quit);
        buttonQuit.setOnClickListener(this);

        if (accountData.getIsLogin()) {
            afterLogin.setVisibility(View.VISIBLE);
            beforeLogin.setVisibility(View.GONE);
        } else {
            afterLogin.setVisibility(View.GONE);
            beforeLogin.setVisibility(View.VISIBLE);
            buttonQuit.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_quit:
                accountData.logoff();
                buttonQuit.setVisibility(View.GONE);
                break;
            case R.id.before_login:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}

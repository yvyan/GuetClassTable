package top.yvyan.guettable.fragment.dayClass;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import top.yvyan.guettable.data.AccountData;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<String> state;
    private Application application;

    public MyViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public MutableLiveData<String> getState() {
        if (state == null) {
            state = new MutableLiveData<>();
            state.setValue(isLogin());
        }
        return state;
    }

    private String isLogin() {
        AccountData accountData = AccountData.newInstance(getApplication());
        if (accountData.getIsLogin()) {
            return "已登录";
        } else {
            return "请登录";
        }
    }
}

package top.yvyan.guettable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.MoreDate;
import top.yvyan.guettable.service.GetDataService;
import top.yvyan.guettable.util.ToastUtil;

public class ExamActivity extends AppCompatActivity {

    private AccountData accountData;
    private GeneralData generalData;
    private MoreDate moreDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        accountData = AccountData.newInstance(this);
        generalData = GeneralData.newInstance(this);
        moreDate = MoreDate.newInstance(this);

        new Thread(() -> {
            List<ExamBean> examBeans;
            String cookie = GetDataService.autoLogin(
                    this,
                    accountData.getUsername(),
                    accountData.getPassword(),
                    generalData.getTerm()
            );
            if (cookie != null) {
                examBeans = GetDataService.getExam(this, cookie, generalData.getTerm());
                if (examBeans != null) {
                    moreDate.setExamBeans(examBeans);
                    runOnUiThread(() -> {
                        ToastUtil.showToast(this, examBeans.toString());
                    });
                }
            }
        }).start();

    }
}
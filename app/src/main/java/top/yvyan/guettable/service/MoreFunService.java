package top.yvyan.guettable.service;

import android.app.Activity;
import android.util.Log;

import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.CookieData;

public class MoreFunService {
    private Activity activity;

    private AccountData accountData;
    private CookieData cookieData;
    private IMoreFun iMoreFun;

    public MoreFunService(Activity activity, IMoreFun iMoreFun) {
        this.activity = activity;
        this.iMoreFun = iMoreFun;
        accountData = AccountData.newInstance(activity);
        cookieData = CookieData.newInstance(activity);
    }

    public void update() {
        new Thread(() -> {
            String cookie;
            if (accountData.getIsLogin()) {
                setView(91); //显示：登录状态检查
                int state = iMoreFun.updateData(cookieData.getCookie());
                Log.d("MoreFunService", "state" + state);
                if (state == 5 || state == -2) { //更新成功或网络错误
                    setView(state);
                    return;
                }
                setView(92); //显示：正在登录
                state = cookieData.refresh();
                if (state != 0) {
                    setView(state);
                    return;
                }
                setView(93); //显示：正在更新
                state = iMoreFun.updateData(cookieData.getCookie());
                setView(state);

            } else {
                setView(2);
            }
        }).start();
    }

    /**
     * state记录当前状态
     *  0 : 登录成功
     *  1 : 登录失效
     *  2 : 未登录
     *
     *  5 : 通用获取数据成功
     *
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     *
     * 21 : 理论课更新成功
     * 22 : 课内实验更新成功
     * 23 : 考试安排更新成功
     *
     * 91 : 登录状态检查
     * 92 : 正在登录
     * 93 : 正在更新
     *
     */
    private void setView(int state) {
        activity.runOnUiThread(() -> {
            iMoreFun.updateView(state);
        });
    }
}

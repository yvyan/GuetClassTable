package top.yvyan.guettable.service.table;

import android.app.Activity;

import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.service.table.IMoreFun;

public class MoreFunService {
    private Activity activity;

    private AccountData accountData;
    private TokenData tokenData;
    private IMoreFun iMoreFun;

    public MoreFunService(Activity activity, IMoreFun iMoreFun) {
        this.activity = activity;
        this.iMoreFun = iMoreFun;
        accountData = AccountData.newInstance(activity);
        tokenData = TokenData.newInstance(activity);
    }

    public void update() {
        new Thread(() -> {
            if (accountData.getIsLogin()) {
                setView(91); //显示：尝试同步
                int state = iMoreFun.updateData(tokenData.getCookie());
                if (state == 5 || state == -2) { //同步成功或网络错误
                    setView(state);
                    return;
                }
                setView(92); //显示：正在登录
                state = tokenData.refresh();
                if (state == -8 || state == -2) {
                    state = tokenData.refresh();
                }
                if (state != 0) {
                    setView(state);
                    return;
                }
                setView(93); //显示：正在同步
                state = iMoreFun.updateData(tokenData.getCookie());
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
     * 91 : 尝试同步
     * 92 : 正在登录
     * 93 : 正在同步
     *
     */
    private void setView(int state) {
        String hint;
        switch (state) {
            case 2:
                hint = "未登录";
                break;
            case -1:
                hint = "密码错误";
                break;
            case -2:
                hint = "网络错误";
                break;
            case 91:
                hint = "尝试同步";
                break;
            case 92:
                hint = "正在登录";
                break;
            case 93:
                hint = "正在同步";
                break;
            case 5:
                hint = "同步成功";
                break;
            default:
                hint = "未知错误";
                break;
        }
        activity.runOnUiThread(() -> {
            iMoreFun.updateView(hint, state);
        });
    }
}
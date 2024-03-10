package top.yvyan.guettable.service;

import android.app.Activity;

import top.yvyan.guettable.data.AccountData;
import top.yvyan.guettable.data.TokenData;

public class MoreFunService {
    private final Activity activity;

    private final AccountData accountData;
    private final TokenData tokenData;
    private final IMoreFun iMoreFun;

    public MoreFunService(Activity activity, IMoreFun iMoreFun) {
        this.activity = activity;
        this.iMoreFun = iMoreFun;
        accountData = AccountData.newInstance(activity);
        tokenData = TokenData.newInstance(activity,this);
    }

    public void update() {
        new Thread(() -> {
            try {
                if (accountData.getIsLogin()) {
                    if(tokenData.tryUpdate(()->updateView(92),()->{
                        updateView(91);
                        int state = iMoreFun.updateData(tokenData.getBkjwCookie());
                        if (state == 5 || state == -2) { //同步成功或网络错误
                            updateView(state);
                        } else {
                            return false;
                        }
                        return true;
                    })) {
                        updateView(5);
                    };
                } else {
                    updateView(2);
                }
            } catch (Exception ignored) {
            }
        }).start();
    }

    /**
     * state记录当前状态
     * 0 : 登录成功
     * 1 : 登录失效
     * 2 : 未登录
     * <p>
     * 5 : 通用获取数据成功
     * <p>
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     * <p>
     * 91 : 尝试同步
     * 92 : 正在登录
     * 93 : 正在同步
     */
    private void updateView(int state) {
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
            case -3:
                hint = "登录失效(点击重试)";
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
        activity.runOnUiThread(() -> iMoreFun.updateView(hint, state));
    }
}

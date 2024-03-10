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
                    if(!tokenData.tryUpdate(()->updateView(92),()->{
                        updateView(91);
                        int state = iMoreFun.updateData(tokenData);
                        if (state == 5 || state == -2) { //同步成功或网络错误
                            updateView(state);
                        } else {
                            return false;
                        }
                        return true;
                    })) {
                        updateView(-5);
                    }
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
        String hint = switch (state) {
            case 2 -> "未登录";
            case -1 -> "密码错误";
            case -2 -> "网络错误";
            case -3 -> "登录失效(点击重试)";
            case 91 -> "尝试同步";
            case 92 -> "正在登录";
            case 93 -> "正在同步";
            case 5 -> "同步成功";
            default -> "未知错误";
        };
        activity.runOnUiThread(() -> iMoreFun.updateView(hint, state));
    }
}

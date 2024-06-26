package top.yvyan.guettable;

import android.app.Application;

import com.tencent.mmkv.MMKV;
import com.xuexiang.xui.XUI;

public class MainApp extends Application {
    private static MainApp myApp = null;

    public static MainApp getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        init();
    }

    private void init() {
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志

        MMKV.initialize(this); //MMKV初始化
    }

}

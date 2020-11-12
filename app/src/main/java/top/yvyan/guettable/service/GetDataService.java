package top.yvyan.guettable.service;

import android.app.Activity;
import android.graphics.Bitmap;

import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.service.fetch.LAN;
import top.yvyan.guettable.util.ToastUtil;

public class GetDataService {

    private StringBuilder cookie_builder = null;

    /**
     * 刷新验证码(后台)
     */
    public void changeCode(Activity activity) {
        cookie_builder = new StringBuilder();
        new Thread(() -> {
            final HttpConnectionAndCode res = LAN.checkCode(activity);
            if (res.obj != null) {
                final String ocr = OCR.getTextFromBitmap(activity, (Bitmap) res.obj, "telephone");
                cookie_builder.append(res.cookie);
            }
        }).start();
    }

    public static void getClassTable(Activity activity, String cookie, String term) {
        new Thread(() -> {
            DayClassFragment.newInstance().updateText("正在获取课表");
            HttpConnectionAndCode classTable = LAN.getClassTable(activity, cookie, term);

            if (classTable.code == 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(activity, classTable.comment.substring(40, 1000));
                    }
                });
            }

            DayClassFragment.newInstance().updateText("正在获取实验...");
            HttpConnectionAndCode labTable = LAN.getLabTable(activity, cookie, term);
            if (labTable.code == 0) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(activity, labTable.comment.substring(40, 1000));
                    }
                });
            }
            DayClassFragment.newInstance().updateText("数据更新成功");
        }).start();
    }
}

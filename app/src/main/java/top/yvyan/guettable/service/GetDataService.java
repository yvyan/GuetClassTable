package top.yvyan.guettable.service;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.bean.StudentInfo;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.service.fetch.LAN;
import top.yvyan.guettable.util.ToastUtil;

public class GetDataService {

    private StringBuilder cookie_builder = null;

    /**
     * 刷新验证码(后台)
     * @param activity 活动
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

    /**
     * 获取基本的学生信息
     * @param activity 活动
     * @param cookie   登录后的cookie
     * @return         基本学生信息
     */
    public static StudentInfo getStudentInfo(Activity activity, String cookie) {
        HttpConnectionAndCode studentInfo = LAN.studentInfo(activity, cookie);
        if (studentInfo.code == 0) {
            StudentInfo studentData = new Gson().fromJson(studentInfo.comment, StudentInfo.class);
            return studentData;
        } else {
            return null;
        }
    }

    /**
     * 首次登录后获取数据信息
     * @param activity 活动
     * @param cookie   登录后的cookie
     * @param term     学期
     */
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

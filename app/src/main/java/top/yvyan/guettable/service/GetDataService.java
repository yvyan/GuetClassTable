package top.yvyan.guettable.service;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.Gson.ClassTable;
import top.yvyan.guettable.Gson.ClassTableOuter;
import top.yvyan.guettable.Gson.LabTable;
import top.yvyan.guettable.Gson.LabTableOuter;
import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.service.fetch.LAN;

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
        ClassData classData = ClassData.newInstance(activity);
        List<CourseBean> courseBeans = new ArrayList<>();
        new Thread(() -> {
            DayClassFragment.newInstance().updateText("正在获取课表");
            HttpConnectionAndCode classTable = LAN.getClassTable(activity, cookie, term);

            if (classTable.code == 0) {
                ClassTableOuter classTableOuter = new Gson().fromJson(classTable.comment, ClassTableOuter.class);
                for (ClassTable classTable1 : classTableOuter.getData()) {
                    courseBeans.add(classTable1.toCourseBean());
                }
                classData.setCourseBeans(courseBeans);
            }

            DayClassFragment.newInstance().updateText("正在获取实验...");
            HttpConnectionAndCode labTable = LAN.getLabTable(activity, cookie, term);
            if (labTable.code == 0) {
                LabTableOuter labTableOuter = new Gson().fromJson(labTable.comment, LabTableOuter.class);
                for (LabTable labTable1 : labTableOuter.getData()) {
                    courseBeans.add(labTable1.toCourseBean());
                }
                classData.setCourseBeans(courseBeans);
                activity.runOnUiThread(() -> {
                    CourseTableFragment.newInstance().updateTable(courseBeans);
                });
            }
            DayClassFragment.newInstance().updateText("数据更新成功");
        }).start();
    }
}

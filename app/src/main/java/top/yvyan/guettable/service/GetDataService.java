package top.yvyan.guettable.service;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import top.yvyan.guettable.Gson.ClassTable;
import top.yvyan.guettable.Gson.ClassTableOuter;
import top.yvyan.guettable.Gson.ExamInfo;
import top.yvyan.guettable.Gson.ExamInfoOuter;
import top.yvyan.guettable.Gson.LabTable;
import top.yvyan.guettable.Gson.LabTableOuter;
import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.data.ClassData;
import top.yvyan.guettable.data.DayClassData;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.fragment.CourseTableFragment;
import top.yvyan.guettable.fragment.DayClassFragment;
import top.yvyan.guettable.service.fetch.LAN;

public class GetDataService {

    /**
     * 刷新验证码(后台)
     * @param activity 活动
     * @return         cookie
     */
    public static String changeCode(Activity activity, StringBuilder cookie_builder) {
        final HttpConnectionAndCode res = LAN.checkCode(activity);
        if (res.obj != null) {
            final String ocr = OCR.getTextFromBitmap(activity, (Bitmap) res.obj, "telephone");
            cookie_builder.append(res.cookie);
            return ocr;
        }
        return null;
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
            activity.runOnUiThread(() -> {
                DayClassFragment.newInstance().updateText("正在获取课表...");
            });
            HttpConnectionAndCode classTable = LAN.getClassTable(activity, cookie, term);

            if (classTable.code == 0) {
                ClassTableOuter classTableOuter = new Gson().fromJson(classTable.comment, ClassTableOuter.class);
                for (ClassTable classTable1 : classTableOuter.getData()) {
                    courseBeans.add(classTable1.toCourseBean());
                }
            } else {
                activity.runOnUiThread(() -> {
                    DayClassFragment.newInstance().updateText("网络错误");
                });
            }

            activity.runOnUiThread(() -> {
                DayClassFragment.newInstance().updateText("正在获取实验...");
            });
            HttpConnectionAndCode labTable = LAN.getLabTable(activity, cookie, term);
            if (labTable.code == 0) {
                LabTableOuter labTableOuter = new Gson().fromJson(labTable.comment, LabTableOuter.class);
                for (LabTable labTable1 : labTableOuter.getData()) {
                    courseBeans.add(labTable1.toCourseBean());
                }
                classData.setCourseBeans(courseBeans);
                activity.runOnUiThread(() -> {
                    CourseTableFragment.newInstance().updateTable(courseBeans);
                    DayClassFragment.newInstance().updateView();
                });
                activity.runOnUiThread(() -> {
                    DayClassFragment.newInstance().updateText("更新成功");
                    GeneralData generalData = GeneralData.newInstance(activity);
                    generalData.setLastUpdateTime(System.currentTimeMillis());
                });
            } else {
                activity.runOnUiThread(() -> {
                    DayClassFragment.newInstance().updateText("网络错误");
                });
            }
        }).start();
    }

    /**
     * 自动更新数据（线程）
     * @param activity
     * @param account
     * @param password
     * @param term
     */
    public static void autoUpdateThread(Activity activity, String account, String password, String term) {
        new Thread(() -> {
            String cookie = autoLogin(activity, account, password, term);
            if (cookie != null) {
                getClassTable(activity, cookie, term);
            }
        }).start();
    }

    /**
     * 自动登录
     * @param activity 活动
     * @param account  学号
     * @param password 密码
     * @param term     学期
     * @return          null: 登录失败
     *                  else: 登录成功后的cookie
     */
    public static String autoLogin(Activity activity, String account, String password, String term) {
        StringBuilder cookie_builder = null;
        /**
         * state记录当前状态
         * 0 : 登录成功
         * 1 : 验证码错误
         * 2 : 密码错误
         * 3 : 网络错误/未知错误
         */
        int state = 1;
        for (int i = 0; i < 3; i++) {
            activity.runOnUiThread(() -> {
                DayClassFragment.newInstance().updateText("尝试登录");
            });
            cookie_builder = new StringBuilder();
            String checkCode = changeCode(activity, cookie_builder);
            HttpConnectionAndCode login_res = LAN.login(activity, account, password, checkCode, cookie_builder.toString(), cookie_builder);
            if (login_res.code != 0) { //登录失败
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    continue;
                } else if (login_res.comment != null && login_res.comment.contains("密码")) {
                    state = 2;
                    activity.runOnUiThread(() -> {
                        DayClassFragment.newInstance().updateText("密码错误");
                    });
                    break;
                } else { //请连接校园网
                    state = 3;
                    activity.runOnUiThread(() -> {
                        DayClassFragment.newInstance().updateText("网络错误");
                    });
                    break;
                }
            } else { //登录成功
                state = 0;
                break;
            }
        }
        if (state == 0) {
            return cookie_builder.toString();
        } else {
            return null;
        }
    }

    public static List<ExamBean> getExam(Activity activity, String cookie, String term) {
        List<ExamBean> examBeans = new ArrayList<>();
        HttpConnectionAndCode examInfo = LAN.getExam(activity, cookie, term);
        if (examInfo.code == 0) {
            ExamInfoOuter examInfoOuter = new Gson().fromJson(examInfo.comment, ExamInfoOuter.class);
            for (ExamInfo examInfo1 : examInfoOuter.getData()) {
                examBeans.add(examInfo1.toExamBean());
            }
        } else {
            return null;
        }
        return examBeans;
    }
}

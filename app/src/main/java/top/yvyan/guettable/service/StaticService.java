package top.yvyan.guettable.service;

import android.content.Context;
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
import top.yvyan.guettable.service.fetch.LAN;

public class StaticService {

    /**
     * 刷新验证码(后台)
     * @param context context
     * @return        cookie
     */
    public static String changeCode(Context context, StringBuilder cookie_builder) {
        final HttpConnectionAndCode res = LAN.checkCode(context);
        if (res.obj != null) {
            final String ocr = OCR.getTextFromBitmap(context, (Bitmap) res.obj, "telephone");
            cookie_builder.append(res.cookie);
            return ocr;
        }
        return null;
    }

    /**
     * 自动登录
     * @param context  context
     * @param account  学号
     * @param password 密码
     * @param cookie_builder cookie
     * @return         state记录当前状态
     *                 0 : 登录成功
     *                 1 : 验证码错误
     *                 2 : 密码错误
     *                 3 : 网络错误/未知错误
     */
    public static int autoLogin(Context context, String account, String password, StringBuilder cookie_builder) {
        /**
         * state记录当前状态
         * 0 : 登录成功
         * 1 : 验证码错误
         * 2 : 密码错误
         * 3 : 网络错误/未知错误
         */
        int state = 1;
        for (int i = 0; i < 4; i++) {
            String checkCode = changeCode(context, cookie_builder);
            HttpConnectionAndCode login_res = LAN.login(context, account, password, checkCode, cookie_builder.toString(), cookie_builder);
            if (login_res.code != 0) { //登录失败
                cookie_builder.delete(0, cookie_builder.length());
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    continue;
                } else if (login_res.comment != null && login_res.comment.contains("密码")) {
                    state = 2;
                    break;
                } else { //请连接校园网
                    state = 3;
                    break;
                }
            } else { //登录成功
                state = 0;
                break;
            }
        }
        return state;
    }

    /**
     * 获取基本的学生信息
     * @param context context
     * @param cookie  登录后的cookie
     * @return        基本学生信息
     */
    public static StudentInfo getStudentInfo(Context context, String cookie) {
        HttpConnectionAndCode studentInfo = LAN.studentInfo(context, cookie);
        if (studentInfo.code == 0) {
            StudentInfo studentData = new Gson().fromJson(studentInfo.comment, StudentInfo.class);
            return studentData;
        } else {
            return null;
        }
    }

    /**
     * 获取理论课程
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return        理论课程列表
     */
    public static List<CourseBean> getClass(Context context, String cookie, String term) {
        HttpConnectionAndCode classTable = LAN.getClassTable(context, cookie, term);
        if (classTable.code == 0) {
            List<CourseBean> courseBeans = new ArrayList<>();
            ClassTableOuter classTableOuter = new Gson().fromJson(classTable.comment, ClassTableOuter.class);
            for (ClassTable classTable1 : classTableOuter.getData()) {
                courseBeans.add(classTable1.toCourseBean());
            }
            return courseBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取课内实验
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return        课内实验列表
     */
    public static List<CourseBean> getLab(Context context, String cookie, String term) {
        HttpConnectionAndCode labTable = LAN.getLabTable(context, cookie, term);
        if (labTable.code == 0) {
            List<CourseBean> courseBeans = new ArrayList<>();
            LabTableOuter labTableOuter = new Gson().fromJson(labTable.comment, LabTableOuter.class);
            for (LabTable labTable1 : labTableOuter.getData()) {
                courseBeans.add(labTable1.toCourseBean());
            }
            return courseBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取考试安排
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return        考试安排列表
     */
    public static List<ExamBean> getExam(Context context, String cookie, String term) {
        List<ExamBean> examBeans = new ArrayList<>();
        HttpConnectionAndCode examInfo = LAN.getExam(context, cookie, term);
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

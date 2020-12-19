package top.yvyan.guettable.service;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.yvyan.guettable.Gson.AvgTeacher;
import top.yvyan.guettable.Gson.AvgTeacherFormGet;
import top.yvyan.guettable.Gson.AvgTeacherFormGetOuter;
import top.yvyan.guettable.Gson.AvgTeacherFormSend;
import top.yvyan.guettable.Gson.AvgTeacherOuter;
import top.yvyan.guettable.Gson.CET;
import top.yvyan.guettable.Gson.CETOuter;
import top.yvyan.guettable.Gson.ClassTable;
import top.yvyan.guettable.Gson.ClassTableOuter;
import top.yvyan.guettable.Gson.EffectiveCredit;
import top.yvyan.guettable.Gson.EffectiveCreditsOuter;
import top.yvyan.guettable.Gson.ExamInfo;
import top.yvyan.guettable.Gson.ExamInfoOuter;
import top.yvyan.guettable.Gson.ExamScore;
import top.yvyan.guettable.Gson.ExamScoreOuter;
import top.yvyan.guettable.Gson.ExperimentScore;
import top.yvyan.guettable.Gson.ExperimentScoreOuter;
import top.yvyan.guettable.Gson.LabTable;
import top.yvyan.guettable.Gson.LabTableOuter;
import top.yvyan.guettable.Gson.PlannedCourse;
import top.yvyan.guettable.Gson.PlannedCoursesOuter;
import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.OCR.OCR;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.service.fetch.LAN;

public class StaticService {

    /**
     * 刷新验证码(后台)
     *
     * @param context context
     * @return cookie
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
     *
     * @param context        context
     * @param account        学号
     * @param password       密码
     * @param cookie_builder cookie
     * @return state记录当前状态
     * 0 : 登录成功
     * -1 : 密码错误
     * -2 : 网络错误/未知错误
     * -3 : 验证码连续错误
     */
    public static int autoLogin(Context context, String account, String password, StringBuilder cookie_builder) {
        int state = 1;
        for (int i = 0; i < 4; i++) {
            String checkCode = changeCode(context, cookie_builder);
            HttpConnectionAndCode login_res = LAN.login(context, account, password, checkCode, cookie_builder.toString(), cookie_builder);
            if (login_res.code != 0) { //登录失败
                cookie_builder.delete(0, cookie_builder.length());
                if (login_res.comment != null && login_res.comment.contains("验证码")) {
                    state = -3;
                } else if (login_res.comment != null && login_res.comment.contains("密码")) {
                    state = -1;
                    break;
                } else { //请连接校园网
                    state = -2;
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
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 基本学生信息
     */
    public static StudentInfo getStudentInfo(Context context, String cookie) {
        HttpConnectionAndCode studentInfo = LAN.studentInfo(context, cookie);
        if (studentInfo.code == 0) {
            return new Gson().fromJson(studentInfo.comment, StudentInfo.class);
        } else {
            return null;
        }
    }

    /**
     * 获取理论课程
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return 理论课程列表
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
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return 课内实验列表
     */
    public static List<CourseBean> getLab(Context context, String cookie, String term) {
        HttpConnectionAndCode labTable = LAN.getLabTable(context, cookie, term);
        if (labTable.code == 0) {
            List<CourseBean> courseBeans = new ArrayList<>();
            LabTableOuter labTableOuter = new Gson().fromJson(labTable.comment, LabTableOuter.class);
            for (LabTable labTable1 : labTableOuter.getData()) {
                CourseBean courseBean = labTable1.toCourseBean();
                if (courseBean.getTime() == 0) {
                    courseBean.setTime(7);
                }
                courseBeans.add(courseBean);
            }
            return courseBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取考试安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期
     * @return 考试安排列表
     */
    public static List<ExamBean> getExam(Context context, String cookie, String term) {
        List<ExamBean> examBeans = new ArrayList<>();
        HttpConnectionAndCode examInfo = LAN.getExam(context, cookie, term);
        if (examInfo.code == 0) {
            ExamInfoOuter examInfoOuter = new Gson().fromJson(examInfo.comment, ExamInfoOuter.class);
            for (ExamInfo examInfo1 : examInfoOuter.getData()) {
                examBeans.add(examInfo1.toExamBean());
            }
            return examBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取等级考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 等级考试成绩列表
     */
    public static List<CETBean> getCET(Context context, String cookie) {
        List<CETBean> cetBeans = new ArrayList<>();
        HttpConnectionAndCode cetInfo = LAN.getCET(context, cookie);
        if (cetInfo.code == 0) {
            CETOuter cetOuter = new Gson().fromJson(cetInfo.comment, CETOuter.class);
            for (CET cet : cetOuter.getData()) {
                cetBeans.add(cet.toCETBean());
            }
            return cetBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取普通考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 普通考试成绩列表
     */
    public static List<ExamScoreBean> getExamScore(Context context, String cookie) {
        List<ExamScoreBean> examScoreBeans = new ArrayList<>();
        HttpConnectionAndCode examScoreInfo = LAN.getExamScore(context, cookie);
        if (examScoreInfo.code == 0) {
            ExamScoreOuter examScoreOuter = new Gson().fromJson(examScoreInfo.comment, ExamScoreOuter.class);
            for (ExamScore examscore1 : examScoreOuter.getData()) {
                examScoreBeans.add(examscore1.toExamScoreBean());
            }
            return examScoreBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取实验考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 验考试成绩列表
     */
    public static List<ExperimentScoreBean> getExperimentScore(Context context, String cookie) {
        List<ExperimentScoreBean> experimentScoreBeans = new ArrayList<>();
        HttpConnectionAndCode experimentScoreInfo = LAN.getExperimentScore(context, cookie);
        if (experimentScoreInfo.code == 0) {
            ExperimentScoreOuter experimentScoreOuter = new Gson().fromJson(experimentScoreInfo.comment, ExperimentScoreOuter.class);
            for (ExperimentScore experimentscore1 : experimentScoreOuter.getData()) {
                experimentScoreBeans.add(experimentscore1.toExperimentScoreBean());
            }
            return experimentScoreBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取当前学期
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 当前学期字符串(例 : 2020 - 2021_1)
     */
    public static String getThisTerm(Context context, String cookie) {
        HttpConnectionAndCode termInfo = LAN.getThisTerm(context, cookie);
        if (termInfo.code == 0) {
            String comment = termInfo.comment;
            int index = comment.indexOf("term");
            return comment.substring(index + 7, index + 18);
        } else {
            return null;
        }
    }

    /**
     * 获取评价教师列表
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期（格式：2020-2021_1，不输入默认当前学期）
     * @return 教师列表
     */
    public static List<AvgTeacher> getTeacherList(Context context, String cookie, String term) {
        if (term == null) {
            term = getThisTerm(context, cookie);
        }
        if (term == null) {
            return null;
        }
        HttpConnectionAndCode teacherList = LAN.getTeacherList(context, cookie, term);
        if (teacherList.code == 0) {
            AvgTeacherOuter avgTeacherOuter = new Gson().fromJson(teacherList.comment, AvgTeacherOuter.class);
            return new ArrayList<>(avgTeacherOuter.getData());
        } else {
            return null;
        }
    }

    /**
     * 获取某个老师的评价表单
     *
     * @param context    context
     * @param cookie     登录后的cookie
     * @param avgTeacher 教师信息类
     * @return 老师评价表单
     */
    public static List<AvgTeacherFormGet> getAvgTeacherForm(Context context, String cookie, AvgTeacher avgTeacher) {
        HttpConnectionAndCode httpConnectionAndCode = LAN.getAvgTeacherForm(context, cookie, avgTeacher.getTerm(), avgTeacher.getCourseno(), avgTeacher.getTeacherno());
        if (httpConnectionAndCode.code == 0) {
            AvgTeacherFormGetOuter avgTeacherFormGetOuter = new Gson().fromJson(httpConnectionAndCode.comment, AvgTeacherFormGetOuter.class);
            return new ArrayList<>(avgTeacherFormGetOuter.getData());
        } else {
            return null;
        }
    }

    /**
     * 提交老师评价表单
     *
     * @param context             context
     * @param cookie              登录后的cookie
     * @param avgTeacherFormSends 评价表单集合
     * @return 结果
     */
    public static String saveTeacherForm(Context context, String cookie, List<AvgTeacherFormSend> avgTeacherFormSends) {
        String postBody = new Gson().toJson(avgTeacherFormSends);
        HttpConnectionAndCode httpConnectionAndCode = LAN.saveTeacherForm(context, cookie, avgTeacherFormSends.get(0).getTerm(), avgTeacherFormSends.get(0).getCourseno(), avgTeacherFormSends.get(0).getTeacherno(), postBody);
        if (httpConnectionAndCode.code == 0) {
            return httpConnectionAndCode.comment;
        }
        return null;
    }

    /**
     * 提交评价老师总评
     *
     * @param context             context
     * @param cookie              登录后的cookie
     * @param avgTeacherFormSends 评价表单
     * @param studentId           学号
     * @param courseName          课程名称
     * @param teacherName         教师名称
     * @param teacherNumber       教师编号
     * @return 操作结果
     */
    public static String commitTeacherForm(Context context, String cookie, List<AvgTeacherFormSend> avgTeacherFormSends, String studentId, String courseName, String teacherName, String teacherNumber) {
        AvgTeacherFormSend avgTeacherFormSend = avgTeacherFormSends.get(0);
        String postBody = "";
        try {
            postBody = "term=" + avgTeacherFormSend.getTerm() + "&courseno=" + avgTeacherFormSend.getCourseno() +
                    "&stid=" + studentId + "&cname=" + URLEncoder.encode(courseName, StandardCharsets.UTF_8.toString()) +
                    "&name=" + URLEncoder.encode(teacherName, StandardCharsets.UTF_8.toString()) +
                    "&teacherno=" + teacherNumber + "&courseid=" + avgTeacherFormSend.getCourseid() +
                    "&lb=" + 1 + "&chk=" + "&can=" + true + "&userid=" + "&bz=" + URLEncoder.encode("老师很好", StandardCharsets.UTF_8.toString()) + "&score=100";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpConnectionAndCode httpConnectionAndCode = LAN.commitTeacherForm(context, cookie, postBody);
        if (httpConnectionAndCode.code == 0) {
            return httpConnectionAndCode.comment;
        }
        return null;
    }

    /**
     * 自动评价一个教师
     *
     * @param context    context
     * @param cookie     登录后的cookie
     * @param avgTeacher 教师信息类
     * @param number     学号
     * @return 0 : 操作成功
     * -1 : 获取评价表单失败
     * -2 : 提交评价表单失败
     * -3 : 提交总评失败
     */
    public static int averageTeacher(Context context, String cookie, AvgTeacher avgTeacher, String number) {
        List<AvgTeacherFormGet> avgTeacherFormGets = getAvgTeacherForm(context, cookie, avgTeacher);
        if (avgTeacherFormGets == null) {
            return -1;
        }
        List<AvgTeacherFormSend> avgTeacherFormSends = new ArrayList<>();
        for (AvgTeacherFormGet avgTeacherFormGet : avgTeacherFormGets) {
            avgTeacherFormSends.add(new AvgTeacherFormSend(avgTeacherFormGet, avgTeacher.getCourseid(), avgTeacher.getCourseno(), avgTeacher.getTeacherno(), avgTeacher.getTerm()));
        }
        String str = StaticService.saveTeacherForm(context, cookie, avgTeacherFormSends);
        if (str != null) {
            str = StaticService.commitTeacherForm(context, cookie, avgTeacherFormSends, number, avgTeacher.getCname(), avgTeacher.getName(), avgTeacher.getTeacherno());
            if (str != null) {
                return 0;
            } else {
                return -3;
            }
        } else {
            return -2;
        }
    }

    /**
     * 获取有效学分
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 有效学分列表
     */
    public static List<EffectiveCredit> getEffectiveCredits(Context context, String cookie) {
        HttpConnectionAndCode updateResult = LAN.updateEffectiveCredits(context, cookie);
        if (updateResult.comment != null && updateResult.comment.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = LAN.getEffectiveCredits(context, cookie);
            if (getResult.code == 0) {
                EffectiveCreditsOuter effectiveCreditsOuter = new Gson().fromJson(getResult.comment, EffectiveCreditsOuter.class);
                return new ArrayList<>(effectiveCreditsOuter.getData());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取计划课程
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 计划课程列表
     */
    public static List<PlannedCourse> getPlannedCourses(Context context, String cookie) {
        HttpConnectionAndCode updateResult = LAN.updateEffectiveCredits(context, cookie);
        if (updateResult.comment != null && updateResult.comment.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = LAN.getPlannedCourses(context, cookie);
            if (getResult.code == 0) {
                PlannedCoursesOuter plannedCoursesOuter = new Gson().fromJson(getResult.comment, PlannedCoursesOuter.class);
                return new ArrayList<>(plannedCoursesOuter.getData());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取计划课程(含限选、任选和通识)
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 计划课程列表(含限选 、 任选和通识)
     */
    public static List<PlannedCourseBean> getPlannedCourseBeans(Context context, String cookie) {
        List<EffectiveCredit> effectiveCredits = getEffectiveCredits(context, cookie);
        List<PlannedCourse> plannedCourses = getPlannedCourses(context, cookie);
        List<PlannedCourseBean> plannedCourseBeans = new ArrayList<>();
        if (effectiveCredits != null && plannedCourses != null) {
            for (PlannedCourse plannedCourse : plannedCourses) {
                plannedCourseBeans.add(plannedCourse.toPlannedCourseBean());
            }
            Collections.sort(effectiveCredits, (effectiveCredit, t1) -> effectiveCredit.getStp().compareTo(t1.getStp()));
            for (EffectiveCredit effectiveCredit : effectiveCredits) {
                String stp = effectiveCredit.getStp();
                if (effectiveCredit.getCname() != null && !"BG".equals(stp) && !"BJ".equals(stp) && !"BS".equals(stp) && !"BT".equals(stp)) {
                    plannedCourseBeans.add(effectiveCredit.toPlannedCourseBean());
                }
            }
            return plannedCourseBeans;
        } else {
            return null;
        }
    }

    /**
     * 计算学分绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param year    学年(例：2020-2021,null:入学至今)
     * @return
     */
    public static float calculateGrades(Context context, String cookie, String year) {
        List<ExamScoreBean> examScoreBeans = getExamScore(context, cookie);
        List<PlannedCourse> plannedCourses = getPlannedCourses(context, cookie);
        if (examScoreBeans != null && plannedCourses != null) {
            //筛选年度
            List<ExamScoreBean> examScoreBeansSelect1 = new ArrayList<>();
            for (ExamScoreBean examScoreBean : examScoreBeans) {
                if (year != null) {
                    if (examScoreBean.getTerm().contains(year)) {
                        examScoreBeansSelect1.add(examScoreBean);
                    }
                } else {
                    examScoreBeansSelect1 = examScoreBeans;
                }
            }
            //筛选重复成绩
            List<ExamScoreBean> examScoreBeansSelect2 = new ArrayList<>();
            List<String> cnos = new ArrayList<>();
            Collections.sort(examScoreBeansSelect1, (examScoreBean12, t1) -> examScoreBean12.getCno().compareTo(t1.getCno()));
            int i = 0;
            for (ExamScoreBean examScoreBean1 : examScoreBeansSelect1) {
                if (!cnos.contains(examScoreBean1.getCno())) {
                    cnos.add(examScoreBean1.getCno());
                    examScoreBeansSelect2.add(examScoreBean1);
                    i++;
                } else {
                    if (examScoreBeansSelect2.get(i - 1).getTotalScore() < examScoreBean1.getTotalScore()) {
                        examScoreBeansSelect2.get(i - 1).setTotalScore(examScoreBean1.getTotalScore()); //取最高总成绩
                    }
                }
            }
            //筛选有效成绩
            float credits = 0;
            float total = 0;
            List<String> cnos1 = new ArrayList<>();
            for (PlannedCourse plannedCourse : plannedCourses) {
                cnos1.add(plannedCourse.getCourseid());
            }
            for (ExamScoreBean examScoreBean2 : examScoreBeansSelect2) {
                if (cnos1.contains(examScoreBean2.getCno()) || examScoreBean2.getType().equals("XZ")) {
                    credits += examScoreBean2.getCredit();
                    total += (examScoreBean2.getCredit() * examScoreBean2.getTotalScore());
                }
            }
            if (credits == 0) {
                return 100;
            } else {
                return total / credits;
            }
        } else {
            return -1;
        }
    }
}

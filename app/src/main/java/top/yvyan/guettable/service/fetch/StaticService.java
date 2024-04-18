package top.yvyan.guettable.service.fetch;

import static java.lang.Math.max;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import top.yvyan.guettable.Gson.BaseResponse;
import top.yvyan.guettable.Gson.CET;
import top.yvyan.guettable.Gson.ClassList;
import top.yvyan.guettable.Gson.ClassTableNew;
import top.yvyan.guettable.Gson.CurrentSemester;
import top.yvyan.guettable.Gson.EffectiveCredit;
import top.yvyan.guettable.Gson.ExamInfo;
import top.yvyan.guettable.Gson.ExamScore;
import top.yvyan.guettable.Gson.ExamScoreNew;
import top.yvyan.guettable.Gson.ExperimentScore;
import top.yvyan.guettable.Gson.Grades;
import top.yvyan.guettable.Gson.LabList;
import top.yvyan.guettable.Gson.LabTableJWT;
import top.yvyan.guettable.Gson.LabTableNew;
import top.yvyan.guettable.Gson.NeedCaptcha;
import top.yvyan.guettable.Gson.PlannedCourse;
import top.yvyan.guettable.Gson.Resit;
import top.yvyan.guettable.Gson.Semester;
import top.yvyan.guettable.Gson.StudentInfo;
import top.yvyan.guettable.Gson.StudentInfoNew;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.bean.CETBean;
import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.bean.ExamScoreBean;
import top.yvyan.guettable.bean.ExperimentScoreBean;
import top.yvyan.guettable.bean.PlannedCourseBean;
import top.yvyan.guettable.bean.ResitBean;
import top.yvyan.guettable.bean.SelectedCourseBean;
import top.yvyan.guettable.bean.TermBean;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.util.VPNUrlUtil;

public class StaticService {

    public static boolean checkNeedCaptcha(Context context, String account) {
        try {
            HttpConnectionAndCode checkCaptcha = Net.checkNeedCaptcha(context, account);
            if (checkCaptcha != null && checkCaptcha.resp_code == 200) {
                NeedCaptcha isNeed = new Gson().fromJson(checkCaptcha.content, NeedCaptcha.class);
                return isNeed.isNeed;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    /**
     * 发送手机验证码
     *
     * @param context   context
     * @param CASCookie CAS Cookie
     * @param account   学号
     * @return Phone Number
     * ERROR0 : 网络错误
     * ERROR1 : 密码错误
     * ERROR2 : 需要使用外网网址进行访问
     * ERROR3 : 验证码发送CD
     */
    public static String reAuth_sendSMSCode(Context context, String account, String CASCookie) {
        HttpConnectionAndCode response = Net.reAuth_sendSMSCode(context, account, CASCookie);
        if (response.code != 0) {
            if (response.code == -5) {
                return "ERROR2";
            }
        } else {
            if (response.content.contains("success")) {
                int PhoneIndex = response.content.indexOf("\"mobile\":\"") + 10;
                return response.content.substring(PhoneIndex, PhoneIndex + 11);
            }
            if (response.content.contains("code_time_fail")) {
                int MessageIndex = response.content.indexOf("\"returnMessage\":\"") + 17;
                String ErrorMessage = response.content.substring(MessageIndex);
                return "ERROR3;" + ErrorMessage.substring(0, ErrorMessage.indexOf("\""));
            }
        }
        return "ERROR0";
    }

    /**
     * 验证手机验证码
     *
     * @param context   context
     * @param CASCookie CAS Cookie
     * @param SMSCode   OTP手机验证码
     * @return 多因素身份验证令牌Cookie
     * ERROR0 : 网络错误
     * ERROR1 : 验证码错误
     * ERROR2 : 需要使用外网网址进行访问
     */
    public static String reAuth_SMSCode(Context context, String SMSCode, String CASCookie) {
        HttpConnectionAndCode response = Net.reAuth_SMSCode(context, SMSCode, CASCookie);
        if (response.code != 0) {
            if (response.code == -5) {
                return "ERROR2";
            }
            return "ERROR0";
        } else {
            if (response.content.contains("reAuth_success")) {
                return response.cookie;
            }
            return "ERROR1";
        }
    }

    /**
     * 获取SSO登录CasCookie
     *
     * @param context   context
     * @param account   学号
     * @param password  密码
     * @param MFACookie MFA Cookie
     * @param TGTToken  TGTToken
     * @return CAS Cookie
     * ERROR0 : 网络错误
     * ERROR1 : 密码错误
     * ERROR2 : 需要使用外网网址进行访问
     * ERROR5 : 2FA Needed
     */
    public static String SSOLogin(Context context, String account, String password, String captcha, String TGTToken, String MFACookie, String SessionCookie) {
        HttpConnectionAndCode response = Net.getCASToken(context, account, password, captcha, TGTToken, MFACookie, SessionCookie);
        if (response.code != 0) {
            if (response.code == 1) {
                String Location = response.c.getHeaderField("location");
                if (Location.contains("reAuthCheck")) {
                    return "ERROR5;" + TGTToken + "; " + response.cookie;
                }
                if (MFACookie!=null && !MFACookie.isEmpty()) {
                    return MFACookie + "; " + TGTToken;
                } else {
                    return TGTToken;
                }
            }
            if (response.code == -8) {
                return "ERROR1";
            }
            return "ERROR0";
        } else {
            String Cookie = response.cookie;
            if (Cookie.contains("TGT-")) {
                String Location = response.c.getHeaderField("location");
                if (Location.contains("reAuthCheck")) {
                    return "ERROR5;" + Cookie;
                }
                return Cookie;
            } else {
                return "ERROR1";
            }
        }
    }

    /**
     * Convert from Golang
     *
     * @param services
     * @param CasCookie
     * @return With Ticket URL AuthURL
     * ERRORNeedlogin needRelogin
     * ERRORNetwork errNetwork
     */
    public static String loginServerBySSO(Context context, String services, String CasCookie) {
        HttpConnectionAndCode response = Net.loginServerBySSO(context, services, CasCookie);
        if (response != null && response.resp_code / 100 == 3 && response.c != null) {
            String Location = response.c.getHeaderField("location");
            if (Location.contains("reAuthCheck")) {
                return "ERRORNeedlogin";
            }
            if (Location.isEmpty()) {
                return "ERRORNetwork";
            }
            return Location;
        }
        if (response != null && response.resp_code / 100 == 2) {
            return "ERRORNeedlogin";
        }
        return "ERRORNetwork";
    }

    /**
     * Convert from Golang
     *
     * @param context
     * @param services
     * @param CASCookie
     * @param VPNCookie
     * @param isVPN
     * @return AuthedCookie
     * ERRORNeedlogin needRelogin
     * ERRORNetwork errNetwork
     */
    public static String authServiceByCas(Context context, String services, String CASCookie, String VPNCookie, Boolean isVPN) {
        String authURL = loginServerBySSO(context, services, CASCookie);
        if (authURL.startsWith("ERROR")) {
            return authURL;
        }
        StringBuilder cookie = new StringBuilder();
        String nextURL = VPNUrlUtil.getVPNUrl(authURL, isVPN);
        while (true) {
            HttpConnectionAndCode response = Net.authService(context, nextURL, isVPN ? VPNCookie : cookie.toString());
            if (response.resp_code / 100 == 3) {
                String Location = response.c.getHeaderField("location");
                if (Location != null) {
                    if (Location.contains("authserver")) {
                        return "ERRORNeedlogin";
                    }
                    if (Location.contains("v.guet.edu.cn/login") || isVPN && Location.equals("/login")) {
                        return "ERRORNeedLogin";
                    }
                }
                if (response.cookie != null && !response.cookie.isEmpty()) {
                    cookie.append(response.cookie).append("; ");
                    nextURL = Location;
                    continue;
                }
            }
            if (response.cookie != null && !response.cookie.isEmpty()) {
                cookie.append(response.cookie);
                return cookie.toString();
            } else {
                return cookie.toString().substring(0, max(0, cookie.length() - 2));
            }

        }
    }

    /**
     * 向VPN添加Cookie
     *
     * @param host     域
     * @param path     路径
     * @param cookie   cookie
     * @param VPNToken VPN Token
     * @return 0 成功
     */
    public static int CookieSet(Context context, String host, String path, String cookie, String VPNToken) {
        HttpConnectionAndCode response = Net.CookieSet(context, host, path, cookie, VPNToken);
        if (response.code == 0) {
            if (response.content.contains("success")) {
                return 0;
            }
        }
        return -1;
    }

    /**
     * 获取补考安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 补考安排列表
     */
    public static List<ResitBean> getResit(Context context, String cookie) {
        List<ResitBean> resitBeans = new ArrayList<>();
        HttpConnectionAndCode resitInfo = Net.getResit(context, cookie, TokenData.isVPN());
        if (resitInfo.code == 0) {
            BaseResponse<List<Resit>> baseResponse = new Gson().fromJson(resitInfo.content, new TypeToken<BaseResponse<List<Resit>>>() {
            }.getType());
            for (Resit resit : baseResponse.getData()) {
                resitBeans.add(resit.toResitBean());
            }
            return resitBeans;
        } else {
            return null;
        }
    }

    /**
     * 获取基本的学生信息
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 基本学生信息
     */
    public static StudentInfoNew getStudentInfoNew(Context context, String cookie) {
        try {
            HttpConnectionAndCode studentInfo = Net.studentInfoNew(context, cookie, TokenData.isVPN());
            if (studentInfo.resp_code == 200) {
                Pattern pattern = Pattern.compile("var ?student ?= ?([^;]+);?\n");
                Matcher matcher = pattern.matcher(studentInfo.content);
                if (matcher.find() && matcher.groupCount() >= 1) {
                    String studentInfotext = matcher.group(1).replace("'", "\"");
                    return new Gson().fromJson(studentInfotext, StudentInfoNew.class);
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取基本的学生信息
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 基本学生信息
     */
    public static StudentInfo getStudentInfo(Context context, String cookie) {
        HttpConnectionAndCode studentInfo = Net.studentInfo(context, cookie, TokenData.isVPN());
        if (studentInfo.code == 0) {
            return new Gson().fromJson(studentInfo.content, StudentInfo.class);
        } else {
            return null;
        }
    }

    /**
     * 获取理论课程
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @return 理论课程列表
     */
    public static List<CourseBean> getClassNew(Context context, String cookie, int semesterId) {
        try {
            List<CourseBean> courseBeans = new ArrayList<>();
            HttpConnectionAndCode classTable = Net.getClassTableNew(context, semesterId, cookie, TokenData.isVPN());
            if (classTable.resp_code == 200) {
                ClassTableNew table = new Gson().fromJson(classTable.content, ClassTableNew.class);
                ClassTableNew.studentTableVms maintable = table.studentTableVms.get(0);
                List<ClassTableNew.ClassTable> lessions = maintable.activities;
                for (ClassTableNew.ClassTable lession : lessions) {
                        List<CourseBean> coursebeans = lession.toCourseBean();
                        courseBeans.addAll(coursebeans);
                }
                if (courseBeans.isEmpty()) {
                    return null;
                }
                return courseBeans;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Semester> getAllSemester(Context context, String cookie) {
        HttpConnectionAndCode classTableIndex = Net.getClassTableIndex(context, cookie, TokenData.isVPN());
        return getAllSemester(classTableIndex);
    }

    public static List<Semester> getAllSemester(HttpConnectionAndCode classTableIndex) {
        if (classTableIndex.resp_code == 200) {
            Pattern pattern = Pattern.compile("var.?semesters.?=.?JSON\\.parse\\([.\n]?[^']*?'([^']*)'");
            Matcher matcher = pattern.matcher(classTableIndex.content);
            if (matcher.find()) {
                String semesters = "\"" + matcher.group(1) + "\"";
                semesters = new Gson().fromJson(semesters, String.class);
                return new Gson().fromJson(semesters, new TypeToken<List<Semester>>() {
                }.getType());
            }
        }
        return null;
    }
    public static CurrentSemester getSemester(Context context, String cookie) {
        HttpConnectionAndCode classTableIndex = Net.getClassTableIndex(context, cookie, TokenData.isVPN());
        return getSemesterJson(classTableIndex);
    }

    private static CurrentSemester getSemesterJson(HttpConnectionAndCode classTableIndex) {
        if (classTableIndex.resp_code == 200) {
            Pattern pattern = Pattern.compile("currentSemester.?=.?([^;]+);");
            Matcher matcher = pattern.matcher(classTableIndex.content);
            if (matcher.find() && matcher.groupCount() >= 1) {
                String currentSemesters = matcher.group(1).replace("'", "\"");
                return new Gson().fromJson(currentSemesters, CurrentSemester.class);
            }
        }
        return null;
    }

    // 十分逆天的 JWT in JWT in JWT
    public static String getLabBridgeJWT(Context context, String cookie) {
        HttpConnectionAndCode labTableRes = Net.getLabBridgeJWT(context, cookie, TokenData.isVPN());
        if (labTableRes.resp_code / 100 == 3) {
            String Location = labTableRes.c.getHeaderField("location");
            Pattern pattern = Pattern.compile("token=(.*?)(?:&|$)");
            Matcher matcher = pattern.matcher(Location);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    public static String getLabJWT(Context context, String cookie) {
        String EDUJWTToken = getLabBridgeJWT(context, cookie);
        if (EDUJWTToken == null) {
            return null;
        }
        HttpConnectionAndCode labTableRes = Net.getLabJWT(context, cookie, EDUJWTToken, TokenData.isVPN());
        if (labTableRes.resp_code / 100 == 2) {
            LabTableJWT labJWT = new Gson().fromJson(labTableRes.content, LabTableJWT.class);
            return labJWT.getToken();
        }
        return null;
    }

    public static Map<String, String> getLabLessonCode(Context context, String jwtToken, String cookie) {
        try {
            HttpConnectionAndCode labIndexRes = Net.getLabIndex(context, jwtToken, cookie, TokenData.isVPN());
            if (labIndexRes.resp_code == 200) {
                LabList labIndex = new Gson().fromJson(labIndexRes.content, LabList.class);
                return labIndex.toMap();
            }
        } catch (Exception ignore) {

        }
        return null;
    }

    public static List<CourseBean> getLabTableNew(Context context, String cookie, String startDate, String endDate) {
        try {
            String jwtToken = getLabJWT(context, cookie);
            if (jwtToken == null) {
                return null;
            }
            Map<String, String> labIndex = getLabLessonCode(context, jwtToken, cookie);
            HttpConnectionAndCode labTableRes = Net.getLabTableNew(context, jwtToken, cookie, startDate, endDate, TokenData.isVPN());
            if (labTableRes.resp_code == 200) {
                LabTableNew labTable = new Gson().fromJson(labTableRes.content, LabTableNew.class);
                return labTable.toCourseBeans(labIndex);
            } else {
                return null;
            }
        } catch (Exception ignore) {
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
        HttpConnectionAndCode examInfo = Net.getExam(context, cookie, term, TokenData.isVPN());
        if (examInfo.code == 0) {
            BaseResponse<List<ExamInfo>> baseResponse = new Gson().fromJson(examInfo.content, new TypeToken<BaseResponse<List<ExamInfo>>>() {
            }.getType());
            for (ExamInfo examInfo1 : baseResponse.getData()) {
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
        HttpConnectionAndCode cetInfo = Net.getCET(context, cookie, TokenData.isVPN());
        if (cetInfo.code == 0) {
            BaseResponse<List<CET>> baseResponse = new Gson().fromJson(cetInfo.content, new TypeToken<BaseResponse<List<CET>>>() {
            }.getType());
            for (CET cet : baseResponse.getData()) {
                cetBeans.add(cet.toCETBean());
            }
            return cetBeans;
        } else {
            return null;
        }
    }

    public static List<ExamBean> getExamNewDirty(Context context, String cookie, String term) {
        return null;
    }

    public static String getExamScoreNewUrl(Context context, String cookie) {
        try {
            HttpConnectionAndCode examScoreUrlInfo = Net.getExamScoreUrlNew(context, cookie, TokenData.isVPN());
            if (examScoreUrlInfo.resp_code / 100 == 3) {
                String Location = examScoreUrlInfo.c.getHeaderField("location");
                String[] subPath = Location.split("/");
                return subPath[max(0, subPath.length - 1)];
            }
        } catch (Exception ignored) {

        }
        return "";
    }

    public static ExamScoreNew getExamScoreNew(Context context, String cookie) {
        try {
            String ExamScoreEndpointId = getExamScoreNewUrl(context, cookie);
            HttpConnectionAndCode examScoreInfo = Net.getExamScoreNew(context, cookie, ExamScoreEndpointId, TokenData.isVPN());
            if (examScoreInfo.resp_code == 200) {
                return new Gson().fromJson(examScoreInfo.content, ExamScoreNew.class);
            }
        } catch (Exception ignored) {
        }
        return null;
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
        HttpConnectionAndCode examScoreInfo = Net.getExamScore(context, cookie, TokenData.isVPN());
        if (examScoreInfo.code == 0) {
            BaseResponse<List<ExamScore>> baseResponse = new Gson().fromJson(examScoreInfo.content, new TypeToken<BaseResponse<List<ExamScore>>>() {
            }.getType());
            for (ExamScore examScore : baseResponse.getData()) {
                examScoreBeans.add(examScore.toExamScoreBean());
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
        HttpConnectionAndCode experimentScoreInfo = Net.getExperimentScore(context, cookie, TokenData.isVPN());
        if (experimentScoreInfo.code == 0) {
            BaseResponse<List<ExperimentScore>> baseResponse = new Gson().fromJson(experimentScoreInfo.content, new TypeToken<BaseResponse<List<ExperimentScore>>>() {
            }.getType());
            for (ExperimentScore experimentScore : baseResponse.getData()) {
                experimentScoreBeans.add(experimentScore.toExperimentScoreBean());
            }
            return experimentScoreBeans;
        } else {
            return null;
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
        HttpConnectionAndCode updateResult = Net.updateEffectiveCredits(context, cookie, TokenData.isVPN());
        if (updateResult.content != null && updateResult.content.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = Net.getEffectiveCredits(context, cookie, TokenData.isVPN());
            if (getResult.code == 0) {
                BaseResponse<List<EffectiveCredit>> baseResponse = new Gson().fromJson(getResult.content, new TypeToken<BaseResponse<List<EffectiveCredit>>>() {
                }.getType());
                return new ArrayList<>(baseResponse.getData());
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
        HttpConnectionAndCode updateResult = Net.updateEffectiveCredits(context, cookie, TokenData.isVPN());
        if (updateResult.content != null && updateResult.content.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = Net.getPlannedCourses(context, cookie, TokenData.isVPN());
            if (getResult.code == 0) {
                BaseResponse<List<PlannedCourse>> baseResponse = new Gson().fromJson(getResult.content, new TypeToken<BaseResponse<List<PlannedCourse>>>() {
                }.getType());
                return new ArrayList<>(baseResponse.getData());
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
     * @param year    年级(例：2018)
     * @return 学分绩
     */
    public static float[] calculateGrades(Context context, String cookie, int year) {
        List<ExamScoreBean> examScoreBeansGet = getExamScore(context, cookie);
        List<PlannedCourse> plannedCoursesGet = getPlannedCourses(context, cookie);
        float[] grades = new float[]{0, 0, 0, 0, 0, 0, 0};
        List<Integer> years = new ArrayList<>();
        years.add(0);
        for (int i = 0; i < 6; i++) {
            years.add(year + i);
        }
        if (examScoreBeansGet != null && plannedCoursesGet != null) {
            int y = 0;
            for (int year2 : years) {
                List<ExamScoreBean> examScoreBeans = new ArrayList<>();
                Collections.addAll(examScoreBeans, new ExamScoreBean[examScoreBeansGet.size()]);
                Collections.copy(examScoreBeans, examScoreBeansGet);
                List<PlannedCourse> plannedCourses = new ArrayList<>();
                Collections.addAll(plannedCourses, new PlannedCourse[plannedCoursesGet.size()]);
                Collections.copy(plannedCourses, plannedCoursesGet);

                //筛选年度
                List<ExamScoreBean> examScoreBeansSelect1 = new ArrayList<>();
                for (ExamScoreBean examScoreBean : examScoreBeans) {
                    if (year2 != 0) {
                        if (examScoreBean.getTerm().contains(String.valueOf(year2)) && examScoreBean.getTerm().contains(String.valueOf(year2 + 1))) {
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
                            examScoreBeansSelect2.remove(i - 1);
                            examScoreBeansSelect2.add(examScoreBean1);
                        }
                    }
                }
                //筛选有效成绩
                float credits = 0;
                float total = 0;
                List<String> cnos1 = new ArrayList<>();
                for (PlannedCourse plannedCourse : plannedCourses) {
                    String id = plannedCourse.getCourseid();
                    if (id.startsWith("B")) {
                        cnos1.add(id);
                    }
                }
                for (ExamScoreBean examScoreBean2 : examScoreBeansSelect2) {
                    if (!examScoreBean2.getScore().equals("旷考") && !examScoreBean2.getScore().equals("取消") && (cnos1.contains(examScoreBean2.getCno()) || examScoreBean2.getType().equals("XZ"))) {
                        credits += examScoreBean2.getCredit();
                        if (examScoreBean2.getType().equals("BS")) {
                            int score = 0;
                            String scoreStr = examScoreBean2.getScore();
                            switch (scoreStr) {
                                case "优":
                                    score = 95;
                                    break;
                                case "良":
                                    score = 85;
                                    break;
                                case "中":
                                    score = 75;
                                    break;
                                case "及格":
                                    score = 65;
                                    break;
                                case "不及格":
                                    score = 40;
                                    break;
                                default:
                                    break;
                            }
                            total += examScoreBean2.getCredit() * score;
                        } else {
                            total += (examScoreBean2.getCredit() * examScoreBean2.getTotalScore());
                        }
                    }
                }
                if (credits == 0) {
                    grades[y] = 100;
                } else {
                    grades[y] = total / credits;
                }
                y++;
            }
            //教务总学分绩替换
            HttpConnectionAndCode httpConnectionAndCode = Net.getGrades(context, cookie, TokenData.isVPN());
            if (httpConnectionAndCode.code == 0) {
                try {
                    BaseResponse<List<Grades>> baseResponse = new Gson().fromJson(httpConnectionAndCode.content, new TypeToken<BaseResponse<List<Grades>>>() {
                    }.getType());
                    if (baseResponse.getData().size() == 1) {
                        grades[0] = baseResponse.getData().get(0).getXfj();
                    }
                } catch (Exception ignored) {
                }
            }
            return grades;
        } else {
            return null;
        }
    }

    /**
     * 查询已选课程
     *
     * @param context context
     * @param cookie  cookie
     * @param term    当前学期
     * @return 操作结果
     */
    public static List<SelectedCourseBean> getSelectedCourse(Context context, String cookie, String term) {
        List<SelectedCourseBean> courseBeans = new ArrayList<>();
        try {
            List<Semester> semesters = getAllSemester(context, cookie);
            for (Semester semester : semesters) {
                HttpConnectionAndCode classTable = Net.getClassList(context, semester.id, cookie, TokenData.isVPN());
                if (classTable.resp_code == 200) {
                    ClassList table = new Gson().fromJson(classTable.content, ClassList.class);
                    List<ClassList.ClassInfo> maintable = table.lessons;
                    for (ClassList.ClassInfo lesson : maintable) {
                        try {
                            SelectedCourseBean course = lesson.toSelectedCourseBean();
                            if (course != null) {
                                courseBeans.add(course);
                            }
                        } catch (Exception ignore){

                        }
                    }
                }
            }
            return courseBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有学期
     *
     * @param context context
     * @param cookie  cookie
     * @return 学期集合
     */
    public static List<TermBean> getTerms(Context context, String cookie) {
        try {
            HttpConnectionAndCode httpConnectionAndCode = Net.getAllTerms(context, cookie, TokenData.isVPN());
            String comment = httpConnectionAndCode.content;
            BaseResponse<List<TermBean>> baseResponse = new Gson().fromJson(comment, new TypeToken<BaseResponse<List<TermBean>>>() {
            }.getType());
            return new ArrayList<>(baseResponse.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

package top.yvyan.guettable.service.fetch;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.yvyan.guettable.Gson.BaseResponse;
import top.yvyan.guettable.Gson.CET;
import top.yvyan.guettable.Gson.ClassTable;
import top.yvyan.guettable.Gson.EffectiveCredit;
import top.yvyan.guettable.Gson.ExamInfo;
import top.yvyan.guettable.Gson.ExamScore;
import top.yvyan.guettable.Gson.ExperimentScore;
import top.yvyan.guettable.Gson.Grades;
import top.yvyan.guettable.Gson.InnovationScore;
import top.yvyan.guettable.Gson.LabTable;
import top.yvyan.guettable.Gson.PlannedCourse;
import top.yvyan.guettable.Gson.Resit;
import top.yvyan.guettable.Gson.SelectedCourse;
import top.yvyan.guettable.Gson.StudentInfo;
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

public class StaticService {

    /**
     * 获取SSO登录CasCookie
     *
     * @param context  context
     * @param account  学号
     * @param password 密码
     * @param VPNToken VPNToken
     * @return CAS Cookie
     * ERROR0 : 网络错误
     * ERROR1 : 密码错误
     * ERROR2 : 需要使用外网网址进行访问
     */
    public static String SSOLogin(Context context, String account, String password, String VPNToken) {
        HttpConnectionAndCode response = Net.getCASToken(context, account, password, VPNToken);
        if (response.code != 0) {
            if (response.code == -5) {
                return "ERROR2";
            }
            return "ERROR0";
        } else {
            String Cookie;
            if(VPNToken == null) {
                Cookie = response.cookie;
            } else {
                Cookie = response.comment;
            }
            if (Cookie.contains("TGT-")) {
                return Cookie;
            } else {
                return "ERROR1";
            }
        }
    }

    /**
     * 获取SSO ST令牌
     *
     * @param context   context
     * @param CASCookie CAS Cookie
     * @param service   ST令牌的服务端
     * @param VPNToken  VPNToken
     * @return ST令牌
     * ERROR0 : 网络错误
     * ERROR1 : TGT失效
     * ERROR2 : 需要使用外网网址进行访问 或 TGT失效(上层调用时，若内网返回此错误，
     * 则先尝试外网，若是TGT失效，则重新获取；若正常获取，则需要将全局网络设置为外网)
     */
    public static String SSOGetST(Context context, String CASCookie, String service, String VPNToken) {
        HttpConnectionAndCode response = Net.getSTbyCas(context, CASCookie, service, VPNToken);
        if (response.code != -7) {
            if (response.code == -5) {
                if (VPNToken != null) {
                    return "ERROR1";
                }
                return "ERROR2";
            }
            if(response.cookie.contains("refresh")) {
                return "ERROR1";
            }
            return "ERROR0";
        } else {
            String Location = response.c.getHeaderField("location");
            if (Location.contains("ST-")) {
                return Location.substring(Location.indexOf("ticket=ST-") + 7);
            }
            return "ERROR1";
        }
    }

    /**
     * 通过ST令牌登录VPN
     *
     * @param ST    ST令牌
     * @param token 用于接收登录后的cookie
     * @return 登录结果
     * 0 -- 登录成功
     * -1 -- 登录失败
     * -2 -- 发生异常
     */
    public static int loginVPNST(String ST, String token) {

        String url = "https://v.guet.edu.cn/https/77726476706e69737468656265737421e6b94689222426557a1dc7af96/login?cas_login=true&ticket=";
        url = url + ST;
        OkHttpClient okHttpClient = new OkHttpClient();
        if (token == null) {
            token = "";
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", token)
                .build();
        final Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            response.close();
            if (response.body() == null || Objects.requireNonNull(response.body()).toString().contains("html lang=\"zh-cmn\"")) {
                return -1;
            } else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 通过ST令牌登录教务系统(VPN)
     *
     * @param ST       ST令牌
     * @param VPNToken VPNToken
     * @return 登录结果
     * 0 -- 登录成功
     * -1 -- VPN登录失败
     * -2 -- 教务登录失败
     * -3 -- 发生异常
     */
    public static int loginBkjwVPNST(String ST, String VPNToken) {

        String url = "https://v.guet.edu.cn/http/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b/?ticket=";
        url = url + ST;
        if (VPNToken == null) {
            VPNToken = "";
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", VPNToken)
                .build();
        final Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            response.close();
            if (response.body() == null || Objects.requireNonNull(response.body()).toString().contains("html lang=\"zh-cmn\"")) {
                return -1;
            } else if (response.body() == null || Objects.requireNonNull(response.body()).toString().contains("统一身份认证平台")) {
                return -2;
            } else {
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
        }
    }

    /**
     * 通过ST令牌登录教务系统(内网)
     *
     * @param context context
     * @param ST      ST令牌
     * @param session 用于接收登录后的cookie
     * @return 登录结果
     * 0 -- 登录成功
     * -1 -- 登录失败
     * -2 -- 网络错误
     */
    public static int loginBkjw(Context context, String ST, StringBuilder session) {
        int state;
        HttpConnectionAndCode login_res = Net.loginBkjwST(context, ST, session);
        if (login_res.code != 0) { //登录失败
            session.delete(0, session.length());
            state = -2;
        } else { //登录成功
            if (login_res.comment.contains("统一身份认证平台")) {
                state = -1;
            } else {
                state = 0;
            }
        }
        return state;
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
        HttpConnectionAndCode resitInfo = Net.getResit(context, cookie, TokenData.isVPN);
        if (resitInfo.code == 0) {
            BaseResponse<List<Resit>> baseResponse = new Gson().fromJson(resitInfo.comment, new TypeToken<BaseResponse<List<Resit>>>() {
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
    public static StudentInfo getStudentInfo(Context context, String cookie) {
        HttpConnectionAndCode studentInfo = Net.studentInfo(context, cookie, TokenData.isVPN);
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
        HttpConnectionAndCode classTable = Net.getClassTable(context, cookie, term, TokenData.isVPN);
        if (classTable.code == 0) {
            List<CourseBean> courseBeans = new ArrayList<>();
            BaseResponse<List<ClassTable>> baseResponse = new Gson().fromJson(classTable.comment, new TypeToken<BaseResponse<List<ClassTable>>>() {
            }.getType());
            for (ClassTable classTable1 : baseResponse.getData()) {
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
        HttpConnectionAndCode labTable = Net.getLabTable(context, cookie, term, TokenData.isVPN);
        if (labTable.code == 0) {
            List<CourseBean> courseBeans = new ArrayList<>();
            BaseResponse<List<LabTable>> baseResponse = new Gson().fromJson(labTable.comment, new TypeToken<BaseResponse<List<LabTable>>>() {
            }.getType());
            for (LabTable labTable1 : baseResponse.getData()) {
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
        HttpConnectionAndCode examInfo = Net.getExam(context, cookie, term, TokenData.isVPN);
        if (examInfo.code == 0) {
            BaseResponse<List<ExamInfo>> baseResponse = new Gson().fromJson(examInfo.comment, new TypeToken<BaseResponse<List<ExamInfo>>>() {
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
        HttpConnectionAndCode cetInfo = Net.getCET(context, cookie, TokenData.isVPN);
        if (cetInfo.code == 0) {
            BaseResponse<List<CET>> baseResponse = new Gson().fromJson(cetInfo.comment, new TypeToken<BaseResponse<List<CET>>>() {
            }.getType());
            for (CET cet : baseResponse.getData()) {
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
        HttpConnectionAndCode examScoreInfo = Net.getExamScore(context, cookie, TokenData.isVPN);
        if (examScoreInfo.code == 0) {
            BaseResponse<List<ExamScore>> baseResponse = new Gson().fromJson(examScoreInfo.comment, new TypeToken<BaseResponse<List<ExamScore>>>() {
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
        HttpConnectionAndCode experimentScoreInfo = Net.getExperimentScore(context, cookie, TokenData.isVPN);
        if (experimentScoreInfo.code == 0) {
            BaseResponse<List<ExperimentScore>> baseResponse = new Gson().fromJson(experimentScoreInfo.comment, new TypeToken<BaseResponse<List<ExperimentScore>>>() {
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
        HttpConnectionAndCode updateResult = Net.updateEffectiveCredits(context, cookie, TokenData.isVPN);
        if (updateResult.comment != null && updateResult.comment.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = Net.getEffectiveCredits(context, cookie, TokenData.isVPN);
            if (getResult.code == 0) {
                BaseResponse<List<EffectiveCredit>> baseResponse = new Gson().fromJson(getResult.comment, new TypeToken<BaseResponse<List<EffectiveCredit>>>() {
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
        HttpConnectionAndCode updateResult = Net.updateEffectiveCredits(context, cookie, TokenData.isVPN);
        if (updateResult.comment != null && updateResult.comment.contains("提取成功")) { //更新成功
            HttpConnectionAndCode getResult = Net.getPlannedCourses(context, cookie, TokenData.isVPN);
            if (getResult.code == 0) {
                BaseResponse<List<PlannedCourse>> baseResponse = new Gson().fromJson(getResult.comment, new TypeToken<BaseResponse<List<PlannedCourse>>>() {
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
            HttpConnectionAndCode httpConnectionAndCode = Net.getGrades(context, cookie, TokenData.isVPN);
            if (httpConnectionAndCode.code == 0) {
                try {
                    BaseResponse<List<Grades>> baseResponse = new Gson().fromJson(httpConnectionAndCode.comment, new TypeToken<BaseResponse<List<Grades>>>() {
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
     * 查询创新积分
     *
     * @param context context
     * @param cookie  cookie
     * @return 操作结果
     */
    public static BaseResponse<InnovationScore> getInnovationScore(Context context, String cookie) {
        HttpConnectionAndCode httpConnectionAndCode = Net.getInnovationScore(context, cookie, TokenData.isVPN);
        if (httpConnectionAndCode.comment != null) {
            BaseResponse<InnovationScore> result;
            try {
                result = new Gson().fromJson(httpConnectionAndCode.comment.replaceAll("[\\[\\]]", ""), new TypeToken<BaseResponse<InnovationScore>>() {
                }.getType());
            } catch (Exception ignored) {
                return null;
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * 更新创新积分
     *
     * @param context context
     * @param cookie  cookie
     * @return 更新结果
     * -1  更新失败
     * 0   更新成功
     */
    public static int updateInnovationScore(Context context, String cookie) {
        HttpConnectionAndCode httpConnectionAndCode = Net.updateInnovationScore(context, cookie, TokenData.isVPN);
        String comment = httpConnectionAndCode.comment;
        if (comment != null && comment.contains("操作成功")) {
            return 0;
        }
        return -1;
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
        try {
            HttpConnectionAndCode httpConnectionAndCode = Net.getSelectedCourse(context, cookie, term, TokenData.isVPN);
            String comment = httpConnectionAndCode.comment;
            List<SelectedCourseBean> list;
            BaseResponse<List<SelectedCourse>> result = new Gson().fromJson(comment, new TypeToken<BaseResponse<List<SelectedCourse>>>() {
            }.getType());
            list = new ArrayList<>();
            for (SelectedCourse selectedCourse : result.getData()) {
                list.add(new SelectedCourseBean(selectedCourse));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            HttpConnectionAndCode httpConnectionAndCode = Net.getAllTerms(context, cookie, TokenData.isVPN);
            String comment = httpConnectionAndCode.comment;
            BaseResponse<List<TermBean>> baseResponse = new Gson().fromJson(comment, new TypeToken<BaseResponse<List<TermBean>>>() {
            }.getType());
            return new ArrayList<>(baseResponse.getData());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

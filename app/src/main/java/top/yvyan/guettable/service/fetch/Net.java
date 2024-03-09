package top.yvyan.guettable.service.fetch;

import static java.lang.Math.max;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.yvyan.guettable.Http.Get;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.Http.Post;
import top.yvyan.guettable.R;
import top.yvyan.guettable.util.AESUtil;
import top.yvyan.guettable.util.RegularUtil;
import top.yvyan.guettable.util.VPNUrlUtil;

public class Net {

    /**
     * 测试连接
     *
     * @return 200 -- 内网
     * else -- 外网
     */
    public static int testNet() {
        String url = "http://10.0.1.5/";
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(1, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            return response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取CAS 登录令牌
     *
     * @param context   context
     * @param account   学号
     * @param password  密码
     * @param TGTToken  CAS-TGT
     * @param MFACookie 2FA二次验证Cookie
     * @return CAS-TGT;
     */
    public static HttpConnectionAndCode getCASToken(Context context, String account, String password, String captcha, String TGTToken, String MFACookie, String SessionCookie) {
        StringBuilder cookie_builder = new StringBuilder();
        String AuthCookie = (TGTToken.isEmpty() ? "" : TGTToken + "; ") + (MFACookie.isEmpty() ? "" : MFACookie + "; ") + (SessionCookie.isEmpty() ? "" : SessionCookie + "; ");
        AuthCookie = AuthCookie.substring(0, max(0, AuthCookie.length() - 2));
        try {
            Resources resources = context.getResources();
            HttpConnectionAndCode loginParams = Get.get(
                    resources.getString(R.string.url_Authserver),
                    null,
                    resources.getString(R.string.user_agent),
                    resources.getString(R.string.SSO_referer),
                    !AuthCookie.equals("") ? AuthCookie : null,
                    null,
                    resources.getString(R.string.cookie_delimiter),
                    null,
                    null,
                    false,
                    null,
                    10000,
                    null);
            if (loginParams.code != 0) {
                if (loginParams.code == -7) {
                    // 已登录或多因子登录验证失效
                    return new HttpConnectionAndCode(loginParams.c, 1, loginParams.content, loginParams.cookie, loginParams.resp_code);
                }
                return new HttpConnectionAndCode(-5);
            }
            cookie_builder.append(loginParams.cookie);
            ArrayList<String> listExp = RegularUtil.getAllSatisfyStr(loginParams.content, "(?<=id=\"pwdEncryptSalt\" value=\")(\\w+)(?=\")");
            String AESKey = listExp.get(0);
            listExp = RegularUtil.getAllSatisfyStr(loginParams.content, "(?<=name=\"execution\" value=\")(.*?)(?=\")");
            String execution = listExp.get(0);
            String body = "username=" + account + "&password=" + URLEncoder.encode(AESUtil.CASEncryption(password, AESKey), "UTF-8") + "&captcha=" + captcha + "&rememberMe=true&_eventId=submit&cllt=userNameLogin&dllt=generalLogin&lt=&execution=" + URLEncoder.encode(execution, "UTF-8");
            HttpConnectionAndCode LoginRequest = Post.post(
                    resources.getString(R.string.url_Authserver),
                    null,
                    resources.getString(R.string.user_agent),
                    resources.getString(R.string.SSO_referer),
                    body,
                    cookie_builder + "; " + MFACookie,
                    "}",
                    resources.getString(R.string.cookie_delimiter),
                    null,
                    null,
                    false,
                    resources.getString(R.string.SSO_context_type));
            if (LoginRequest.resp_code == 401) {
                return new HttpConnectionAndCode(-8); //密码错误
            }
            if (LoginRequest.code == -7) {
                List<String> cookies = LoginRequest.c.getHeaderFields().get("Set-Cookie");
                if (cookies != null) {
                    cookie_builder.append("; ");
                    for (String cookie_resp : cookies) {
                        cookie_builder.append(cookie_resp.substring(0, cookie_resp.indexOf(";") + 1)).append(" ");
                    }
                }
                LoginRequest.cookie = cookie_builder.substring(0, max(0, cookie_builder.length() - 2));
                LoginRequest.code = 0;
                return LoginRequest;
            }
            return LoginRequest;
        } catch (Exception ignored) {

        }
        return new HttpConnectionAndCode(-5);
    }

    /**
     * 获取手机验证码
     *
     * @param context   context
     * @param CASCookie CAS Cookie
     * @param account   account
     * @return Response
     */
    public static HttpConnectionAndCode reAuth_sendSMSCode(Context context, String account, String CASCookie) {
        Resources resources = context.getResources();
        return Post.post(
                resources.getString(R.string.url_SendSMSCode),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                "userName=" + account + "&authCodeTypeName=reAuthDynamicCodeType",
                CASCookie,
                null,
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                false,
                resources.getString(R.string.SSO_context_type));
    }

    /**
     * 认证手机验证码
     *
     * @param context   context
     * @param CASCookie CAS Cookie
     * @param OTP       OTP
     * @return Response
     */
    public static HttpConnectionAndCode reAuth_SMSCode(Context context, String OTP, String CASCookie) {
        Resources resources = context.getResources();
        return Post.post(
                resources.getString(R.string.url_ReAuth),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                "service=&reAuthType=3&isMultifactor=true&password=&dynamicCode=" + OTP + "&uuid=&answer1=&answer2=&otpCode=&skipTmpReAuth=true",
                CASCookie,
                null,
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                false,
                resources.getString(R.string.SSO_context_type));
    }

    /**
     * 使用 SSO 服务器认证服务
     *
     * @param service   要认证的服务
     * @param CASCookie CAS Cookie
     * @return Response
     */
    public static HttpConnectionAndCode loginServerBySSO(Context context, String service, String CASCookie) {
        Resources resources = context.getResources();
        try {
            return Get.get(
                    "https://cas.guet.edu.cn/authserver/login?service=" + URLEncoder.encode(service, "utf-8"),
                    null,
                    resources.getString(R.string.user_agent),
                    resources.getString(R.string.SSO_referer),
                    CASCookie,
                    null,
                    resources.getString(R.string.cookie_delimiter),
                    null,
                    null,
                    false,
                    null,
                    3000,
                    null);
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpConnectionAndCode checkNeedCaptcha(Context context, String account) {
        try {
            Resources resources = context.getResources();
            return Get.get(
                    "https://cas.guet.edu.cn/authserver/checkNeedCaptcha.htl?username" + URLEncoder.encode(account, "UTF-8") + "&_=" + System.currentTimeMillis(),
                    null,
                    resources.getString(R.string.user_agent),
                    resources.getString(R.string.SSO_referer),
                    "",
                    null,
                    resources.getString(R.string.cookie_delimiter),
                    null,
                    null,
                    false,
                    null,
                    3000,
                    null);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 请求认证地址获取认证Cookie
     *
     * @param authurl 认证URL
     * @param cookie  传递的cookie
     * @return Response
     */
    public static HttpConnectionAndCode authService(Context context, String authurl, String cookie) {
        Resources resources = context.getResources();
        return Get.get(
                authurl,
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                cookie,
                null,
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                false,
                null,
                3000,
                null);
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
    public static HttpConnectionAndCode CookieSet(Context context, String host, String path, String cookie, String VPNToken) {
        Resources resources = context.getResources();
        try {
            return Get.get(
                    "https://v.guet.edu.cn/wengine-vpn/cookie?method=set" + "&host=" + host +
                            "&path=" + path +
                            "&scheme=https&ck_data=" + URLEncoder.encode(cookie, "UTF-8"),
                    null,
                    resources.getString(R.string.user_agent),
                    "https://v.guet.edu.cn",
                    VPNToken,
                    null,
                    resources.getString(R.string.cookie_delimiter),
                    null,
                    null,
                    false,
                    null,
                    3000,
                    null);
        } catch (Exception ignore) {
        }
        return new HttpConnectionAndCode(-1);
    }

    /**
     * 获取学生个人信息
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的个人信息
     */
    public static HttpConnectionAndCode studentInfo(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_student_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                null,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_get_student_success_contain_response_text),
                null,
                null,
                null
        );
    }

    public static HttpConnectionAndCode getClassList(Context context, int semesterId, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl(String.format("https://bkjwtest.guet.edu.cn/student/for-std/course-table/get-data?bizTypeId=2&semesterId=%d&dataId=", semesterId), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/for-std/course-table", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                10000,
                null
        );
    }

    public static HttpConnectionAndCode getClassTableNew(Context context, int semesterId, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl(String.format("https://bkjwtest.guet.edu.cn/student/for-std/course-table/semester/%d/print-data?semesterId=%d&hasExperiment=true", semesterId, semesterId), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/for-std/course-table", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                10000,
                null
        );
    }

    public static HttpConnectionAndCode getCurrentTechWeek(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home/get-current-teach-week", isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                10000,
                null
        );
    }

    public static HttpConnectionAndCode getSemesterById(Context context, int id, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/ws/semester/get/" + id, isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                10000,
                null
        );
    }

    public static HttpConnectionAndCode getClassTableIndex(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/for-std/course-table", isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                10000,
                null
        );
    }

    /**
     * 获取课内实验安排
     *
     * @param context  context
     * @param jwtToken jwt令牌
     * @param isVPN    是否为外网
     * @return
     */
    public static HttpConnectionAndCode getLabTableNew(Context context, String jwtToken, String cookie, String startDate, String endDate, boolean isVPN) {
        Resources resources = context.getResources();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-access-token", jwtToken);
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/guet-lab-system/schedule/mesCourseScheduleItem/queryScheduleInfo?_t=" + System.currentTimeMillis() / 1000 + "&type=6&startDate=" + startDate + "&endDate=" + endDate, isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                null,
                null,
                30000,
                null,
                headers
        );
    }

    /**
     * 获取考试安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param term    学期（格式：2020-2021_1）
     * @param isVPN   是否为外网
     * @return gson格式的考试安排
     */
    public static HttpConnectionAndCode getExam(Context context, String cookie, String term, boolean isVPN) {
        Resources resources = context.getResources();
        String[] param = {"term=" + term};
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_exam_url), isVPN),
                param,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                15000,
                15000,
                null
        );
    }

    /**
     * 获取补考安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的补考安排
     */
    public static HttpConnectionAndCode getResit(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_resit_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 获取等级考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的等级考试成绩
     */
    public static HttpConnectionAndCode getCET(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_cet_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 获取普通考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的普通考试成绩
     */
    public static HttpConnectionAndCode getExamScore(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_examscore_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                10000,
                null
        );
    }

    /**
     * 获取实验考试成绩
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的实验考试成绩
     */
    public static HttpConnectionAndCode getExperimentScore(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_experimentscore_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 同步有效学分
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode updateEffectiveCredits(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_update_effective_credits), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                null,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null
        );
    }

    /**
     * 获取有效学分
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getEffectiveCredits(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_effective_credits), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 获取计划课程
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getPlannedCourses(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_planned_credits), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                10000,
                null
        );
    }


    /**
     * 获取学生个人信息
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的个人信息
     */
    public static HttpConnectionAndCode getGrades(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_grades_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_grades_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 获取所有学期
     *
     * @param context context
     * @param cookie  cookie
     * @param isVPN   isVPN
     * @return 所有学期信息
     */
    public static HttpConnectionAndCode getAllTerms(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn" + resources.getString(R.string.lan_get_terms_url), isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn", isVPN),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_grades_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    public static HttpConnectionAndCode getLabJWT(Context context, String cookie, String jwtEduToken, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/guet-lab-system/api/authentication/getAccessTokenByEduToken?_t=" + System.currentTimeMillis() / 1000 + "&token=" + jwtEduToken, isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null
        );
    }

    public static HttpConnectionAndCode getLabBridgeJWT(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/for-std/extra-system/newcapec-experiment/mesMySchedule", isVPN),
                null,
                resources.getString(R.string.user_agent),
                VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn", isVPN),
                cookie,
                null,
                null,
                null,
                null,
                false,
                null,
                null,
                null
        );
    }
}

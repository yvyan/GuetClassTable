package top.yvyan.guettable.service.table.fetch;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import top.yvyan.guettable.Gson.LoginResponse;
import top.yvyan.guettable.Http.Get;
import top.yvyan.guettable.Http.GetBitmap;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.Http.Post;
import top.yvyan.guettable.R;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.util.RSAUtil;
import top.yvyan.guettable.util.UrlReplaceUtil;

public class Net {

    /**
     * 测试连接
     *
     * @param context context
     * @return 0 -- 内网
     * else -- 外网
     */
    public static int testNet(Context context) {
        Resources resources = context.getResources();
        HttpConnectionAndCode response = Get.get(
                "http://172.16.13.22/",
                null,
                resources.getString(R.string.user_agent),
                "http://172.16.13.22/",
                null,
                "]}",
                null,
                null,
                null,
                null,
                200,
                200,
                null
        );
        return response.code;
    }

    /**
     * 获取VPNToken
     *
     * @param context context
     * @return VPNToken
     */
    public static String getVPNToken(Context context) {
        Resources resources = context.getResources();
        HttpConnectionAndCode get_res = Get.get(
                resources.getString(R.string.vpn_url),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.vpn_refer),
                null,
                null,
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                true,
                2000,
                2000,
                null
        );
        return get_res.cookie;
    }

    /**
     * 获取SSO TGT令牌
     *
     * @param context  context
     * @param account  学号
     * @param password 密码
     * @param isVPN    是否外网登录
     * @return TGT令牌
     */
    public static HttpConnectionAndCode getTGT(Context context, String account, String password, boolean isVPN) {
        Resources resources = context.getResources();
        password = new StringBuffer(password).reverse().toString(); //密码倒序
        password = RSAUtil.CASEncryption(password); //密码加密
        String body = "username=" + account + "&password=" + password + "&service=http%3A%2F%2Ficampus.guet.edu.cn%2FCampusPortal&loginType=";
        return Post.post(
                isVPN ? resources.getString(R.string.url_get_TGT_vpn) : resources.getString(R.string.url_get_TGT),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                body,
                null,
                "}",
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                null,
                resources.getString(R.string.SSO_context_type)
        );
    }

    /**
     * 获取SSO ST令牌
     *
     * @param context context
     * @param TGT     TGT令牌
     * @param service ST令牌的服务端
     * @param isVPN   是否外网登录
     * @return ST令牌
     */
    public static HttpConnectionAndCode getST(Context context, String TGT, String service, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                isVPN ? resources.getString(R.string.url_get_ST_vpn) + TGT : resources.getString(R.string.url_get_ST) + TGT,
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                service,
                null,
                "}",
                resources.getString(R.string.cookie_delimiter),
                resources.getString(R.string.SSO_success_contain_ST),
                null,
                null,
                resources.getString(R.string.SSO_context_type)
        );
    }

    /**
     * 通过ST令牌登录教务系统(内网)
     *
     * @param context context
     * @param ST      ST令牌
     * @param session 用于接收登录后的cookie
     * @return 登录状态
     */
    public static HttpConnectionAndCode loginBkjwST(Context context, String ST, StringBuilder session) {
        Resources resources = context.getResources();
        String[] param = {"ticket=" + ST};
        HttpConnectionAndCode login_res = Get.get(
                resources.getString(R.string.SSO_bkjw),
                param,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.SSO_referer),
                null,
                "]}",
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                null,
                null,
                5000,
                resources.getString(R.string.SSO_context_type)
        );
        if (login_res.code == 0) {
            assert session != null;
            session.append(login_res.cookie);
        }
        return login_res;
    }

    /**
     * 密码登录VPN
     *
     * @param context  context
     * @param VPNToken VPNToken
     * @param account  学号
     * @param password 密码
     * @return 操作返回数据
     */
    public static HttpConnectionAndCode loginVPN(Context context, String VPNToken, String account, String password) {
        Resources resources = context.getResources();
        password = RSAUtil.VPNEncryption(password); //密码加密
        String body = "auth_type=local&username=" + account + "&sms_code=&password=" + password + "&captcha=&needCaptcha=false";
        return Post.post(
                resources.getString(R.string.VPN_login),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.VPN_referer),
                body,
                VPNToken,
                "}",
                resources.getString(R.string.cookie_delimiter),
                null,
                null,
                null,
                resources.getString(R.string.VPN_context_type)
        );
    }

    /**
     * 获取验证码
     *
     * @param context  context
     * @param VPNToken VPNToken null表示内网
     * @return 验证码图片
     */
    public static HttpConnectionAndCode checkCode(Context context, String VPNToken) {
        Resources resources = context.getResources();
        return GetBitmap.get(
                UrlReplaceUtil.getUrlByVPN(VPNToken != null, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_checkcode_url))),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.lan_get_checkcode_referer),
                VPNToken,
                resources.getString(R.string.cookie_delimiter)
        );
    }

    /**
     * 登录
     *
     * @param context   context
     * @param account   学号
     * @param pwd       密码
     * @param checkCode 验证码
     * @param cookie    获取验证码之后的cookie / 外网:获取VPNToken
     * @param builder   用于接收登录后的cookie
     * @param isVPN     是否为外网
     * @return 登录状态
     */
    public static HttpConnectionAndCode login(Context context, String account, String pwd, String checkCode, String cookie, StringBuilder builder, boolean isVPN) {
        Resources resources = context.getResources();
        String body = "us=" + account + "&pwd=" + pwd + "&ck=" + checkCode;
        HttpConnectionAndCode login_res = Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_login_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_login_url))),
                body,
                cookie,
                "}",
                resources.getString(R.string.cookie_delimiter),
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
        if (login_res.code == 0) {
            LoginResponse response = new Gson().fromJson(login_res.comment, LoginResponse.class);
            login_res.comment = response.getMsg();
        }
        if (login_res.code == 0 && isVPN) {
            return login_res;
        }
        if (login_res.code == 0 && builder != null) {
            if (!builder.toString().isEmpty()) {
                builder.append(resources.getString(R.string.cookie_delimiter));
            }
            builder.append(login_res.cookie);
        }
        return login_res;
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_student_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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

    /**
     * 获取课程安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的课程安排
     */
    public static HttpConnectionAndCode getClassTable(Context context, String cookie, String term, boolean isVPN) {
        Resources resources = context.getResources();
        String[] param = {"term=" + term};
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_table_url))),
                param,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
     * 获取课内实验安排
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @param term    学期（格式：2020-2021_1）
     * @return gson格式的课内实验安排
     */
    public static HttpConnectionAndCode getLabTable(Context context, String cookie, String term, boolean isVPN) {
        Resources resources = context.getResources();
        String[] param = {"term=" + term};
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_lab_table_url))),
                param,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_get_table_success_contain_response_text),
                null,
                null,
                null,
                30000,
                null
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_exam_url))),
                param,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_resit_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_cet_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_examscore_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_experimentscore_url))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
     * 获取当前学期
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return gson格式的当前学期
     */
    public static HttpConnectionAndCode getThisTerm(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_this_term))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                null,
                cookie,
                null,
                resources.getString(R.string.cookie_delimiter),
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
    }

    /**
     * 获取评价教师列表
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @param term    学期（格式：2020-2021_1）
     * @return gson格式的教师列表
     */
    public static HttpConnectionAndCode getTeacherList(Context context, String cookie, String term, boolean isVPN) {
        Resources resources = context.getResources();
        String[] param = {"term=" + term};
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_teacher_list))),
                param,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                10000,
                null
        );
    }

    /**
     * 获取某个老师的评价表单
     *
     * @param context   context
     * @param cookie    登录后的cookie
     * @param term      学期（格式：2020-2021_1）
     * @param courseNo  课程编号
     * @param teacherNo 老师编号
     * @param isVPN     是否为外网
     * @return gson格式的老师评价表单
     */
    public static HttpConnectionAndCode getAvgTeacherForm(Context context, String cookie, String term, String courseNo, String teacherNo, boolean isVPN) {
        Resources resources = context.getResources();
        String[] params = {
                "term=" + term,
                "courseno=" + courseNo,
                "teacherno=" + teacherNo
        };
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_avg_thacher_data))),
                params,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 提交老师评价表单
     *
     * @param context   context
     * @param cookie    登录后的cookie
     * @param term      学期（格式：2020-2021_1）
     * @param courseNo  课程编号
     * @param teacherNo 老师编号
     * @param postBody  评价表单
     * @param isVPN     是否为外网
     * @return 结果
     */
    public static HttpConnectionAndCode saveTeacherForm(Context context, String cookie, String term, String courseNo, String teacherNo, String postBody, boolean isVPN) {
        Resources resources = context.getResources();
        String[] params = {
                "term=" + term,
                "courseno" + courseNo,
                "teacherno" + teacherNo
        };
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_save_avg_teacher_data))),
                params,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                postBody,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                "application/json"
        );
    }

    /**
     * 提交评价老师总评
     *
     * @param context  context
     * @param cookie   登录后的cookie
     * @param postBody 请求体
     * @param isVPN    是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode commitTeacherForm(Context context, String cookie, String postBody, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_commit_avg_teacher_data))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                postBody,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
    }

    /**
     * 同步有效课程
     *
     * @param context context
     * @param cookie  登录后的cookie
     * @param isVPN   是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode updateEffectiveCredits(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_update_effective_credits))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_effective_credits))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_get_planned_credits))),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
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
     * 获取待评教材列表
     *
     * @param context context
     * @param cookie  登陆后cookie
     * @param isVPN   是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getTextbookList(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_get_textbook_list)),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                10000,
                null
        );
    }

    /**
     * 获取教材评价信息表
     *
     * @param context  context
     * @param cookie   登录后cookie
     * @param term     学期
     * @param courseid 课程代码
     * @param isVPN    是否为外网
     * @param lsh      参数
     * @return 操作结果
     */
    public static HttpConnectionAndCode getAvgTextbookFormOuter(Context context, String cookie, String term, String courseid, int lsh, boolean isVPN) {
        Resources resources = context.getResources();
        String[] params = {
                "term=".concat(term),
                "courseid=".concat(courseid),
                "lsh=".concat(String.valueOf(lsh))
        };
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_get_textbook_form)),
                params,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 获取教材评价状态
     *
     * @param context  context
     * @param cookie   登录后cookie
     * @param term     学期
     * @param courseid 课程代码
     * @param lsh      参数
     * @param isVPN    是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getAvgTextbookFormState(Context context, String cookie, String term, String courseid, int lsh, boolean isVPN) {
        Resources resources = context.getResources();
        String postBody = "term=" + term + "&courseid=" + courseid + "&lsh=" + lsh;
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_get_textbook_avg_state)),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                postBody,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
    }

    /**
     * 保存教材评价表单
     *
     * @param context  context
     * @param cookie   cookie
     * @param term     学期
     * @param courseid 课程代码
     * @param lsh      参数
     * @param postBody 请求体参数
     * @param isVPN    是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode saveTextbookForm(Context context, String cookie, String term, String courseid, int lsh, String postBody, boolean isVPN) {
        Resources resources = context.getResources();
        String[] params = {
                "term=".concat(term),
                "courseid=".concat(courseid),
                "lsh=".concat(String.valueOf(lsh))
        };
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_save_avg_textbook_data)),
                params,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                postBody,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                "application/json"
        );
    }

    /**
     * 提交教材评价
     *
     * @param context  context
     * @param cookie   cookie
     * @param postBody 请求体参数
     * @param isVPN    是否为外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode commitTextbookForm(Context context, String cookie, String postBody, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_commit_avg_textbook_data)),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                postBody,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
    }

    /**
     * 查询创新积分
     *
     * @param context context
     * @param cookie  cookie
     * @param isVPN   是否外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getInnovationScore(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Get.get(UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_get_innovationScore)),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * 更新创新积分
     *
     * @param context context
     * @param cookie  cookie
     * @param isVPN   是否外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode updateInnovationScore(Context context, String cookie, boolean isVPN) {
        Resources resources = context.getResources();
        return Post.post(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_update_innovationScore)),
                null,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                null,
                cookie,
                "}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null
        );
    }


    /**
     * 查询已选课程
     *
     * @param context context
     * @param cookie  cookie
     * @param isVPN   是否外网
     * @return 操作结果
     */
    public static HttpConnectionAndCode getSelectedCourse(Context context, String cookie, String term, boolean isVPN) {
        Resources resources = context.getResources();
        String[] param = {"term=".concat(term)};
        return Get.get(
                UrlReplaceUtil.getUrlByVPN(isVPN, resources.getString(R.string.lan_get_selected_course)),
                param,
                resources.getString(R.string.user_agent),
                UrlReplaceUtil.getUrlByVPN(isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(context).isInternational(), resources.getString(R.string.lan_referer))),
                cookie,
                "]}",
                null,
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null,
                null,
                null,
                null
        );
    }
}

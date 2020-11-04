package top.yvyan.guettable.fetch;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.Gson;

import top.yvyan.guettable.Gson.LoginResponse;
import top.yvyan.guettable.Http.GetBitmap;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
import top.yvyan.guettable.Http.Post;
import top.yvyan.guettable.R;

public class LAN {

    /**
     * 获取验证码
     * @param context
     * @return
     */
    public static HttpConnectionAndCode checkCode(Context context) {
        Resources resources = context.getResources();
        return GetBitmap.get(
                resources.getString(R.string.lan_get_checkcode_url),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.lan_get_checkcode_referer),
                null,
                resources.getString(R.string.cookie_delimiter)
        );
    }

    /**
     * 登陆
     * @param context
     * @param account
     * @param pwd
     * @param checkCode
     * @param cookie
     * @param builder
     * @return
     */
    public static HttpConnectionAndCode login(Context context, String account, String pwd, String checkCode, String cookie, StringBuilder builder) {
        Resources resources = context.getResources();
        String body = "us=" + account + "&pwd=" + pwd + "&ck=" + checkCode;
        HttpConnectionAndCode login_res = Post.post(
                resources.getString(R.string.lan_login_url),
                null,
                resources.getString(R.string.user_agent),
                resources.getString(R.string.lan_login_url),
                body,
                cookie,
                "}",
                resources.getString(R.string.cookie_delimiter),
                resources.getString(R.string.lan_login_success_contain_response_text),
                null,
                null
        );
        if (login_res.code == 0) {
            LoginResponse response = new Gson().fromJson(login_res.comment, LoginResponse.class);
            login_res.comment = response.getMsg();
        }
        if (login_res.code == 0 && builder != null) {
            if (!builder.toString().isEmpty()) {
                builder.append(resources.getString(R.string.cookie_delimiter));
            }
            builder.append(login_res.cookie);
        }
        return login_res;
    }
}

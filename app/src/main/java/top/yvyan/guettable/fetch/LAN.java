package top.yvyan.guettable.fetch;

import android.content.Context;
import android.content.res.Resources;

import top.yvyan.guettable.Http.GetBitmap;
import top.yvyan.guettable.Http.HttpConnectionAndCode;
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
}

package top.yvayn.guettable.Http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class GetBitmap {
    /**
     * @non-ui
     * @return
     * - 0 GET success
     * - -1 cannot open url
     * - -5 cannot get response
     * - -6 response check fail
     * - -7 302
     * @clear
     */
    public static HttpConnectionAndCode get(@NonNull final String u,
                                            @Nullable final String[] parms,
                                            @NonNull final String user_agent,
                                            @NonNull final String referer,
                                            @Nullable final String cookie,
                                            @Nullable final String cookie_delimiter){
        URL url = null;
        HttpURLConnection cnt = null;
        String response = null;
        Bitmap bmp = null;
        int resp_code = 0;
        try {
            StringBuilder u_bulider = new StringBuilder();
            u_bulider.append(u);
            if (parms != null && parms.length > 0) {
                u_bulider.append("?").append(TextUtils.join("&", parms));
            }
            url = new URL(u_bulider.toString());
            cnt = (HttpURLConnection) url.openConnection();
            cnt.setDoOutput(true);
            cnt.setDoInput(true);
            cnt.setRequestProperty("User-Agent", user_agent);
            cnt.setRequestProperty("Referer", referer);
            if (cookie != null){
                cnt.setRequestProperty("Cookie", cookie);
            }
            cnt.setRequestMethod("GET");
            cnt.setInstanceFollowRedirects(false);
            cnt.setReadTimeout(4000);
            cnt.setConnectTimeout(2000);
            cnt.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-1);
        }
        try {
            resp_code = cnt.getResponseCode();
            if (resp_code == 302){
                return new HttpConnectionAndCode(cnt, -7, "");
            }
            response = "";
            bmp = BitmapFactory.decodeStream(cnt.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-5);
        }

        //get cookie from server
        String set_cookie = null;
        if (cookie_delimiter != null) {
            CookieManager cookieman = new CookieManager();
            StringBuilder cookie_builder = new StringBuilder();
            //getHeaderFields() returns the header fields of response
            List<String> cookies = cnt.getHeaderFields().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie_resp : cookies) {
                    cookieman.getCookieStore().add(null, HttpCookie.parse(cookie_resp).get(0));
                }
            }
            if (cookieman.getCookieStore().getCookies().size() > 0) {
                List<HttpCookie> cookieList = cookieman.getCookieStore().getCookies();
                List<String> cookieStringList = new LinkedList<>();
                for (HttpCookie httpCookie : cookieList){
                    String str = httpCookie.getName() + "=" + httpCookie.getValue();
                    cookieStringList.add(str);
                }
                String cookie_join = TextUtils.join(cookie_delimiter, cookieStringList);
                cookie_builder.append(cookie_join);
            }
            set_cookie = cookie_builder.toString();
        }

        //do not disconnect, keep alive
        //if cookie_delimiter != null but no server cookie, set_cookie = ""
        //if no response, response = ""
        HttpConnectionAndCode res = new HttpConnectionAndCode(cnt, 0, response, set_cookie, resp_code);
        res.obj = bmp;
        return res;
    }
}

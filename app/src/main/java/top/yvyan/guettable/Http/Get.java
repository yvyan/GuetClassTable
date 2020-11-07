package top.yvyan.guettable.Http;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;


public class Get {
    /**
     * @non-ui
     * @return
     * - 0 GET success
     * - -1 cannot open url
     * - -2 cannot close input stream
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
                                            @Nullable final String tail,
                                            @Nullable final String cookie_delimiter,
                                            @Nullable final String success_resp_text,
                                            @Nullable final String[] accept_encodings,
                                            @Nullable final Boolean redirect,
                                            @Nullable final Integer connect_timeout,
                                            @Nullable final Integer read_timeout
                                            ){
        URL url = null;
        HttpURLConnection cnt = null;
        DataOutputStream dos = null;
        InputStreamReader in = null;
        String response = null;
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
            if (accept_encodings != null && accept_encodings.length > 0){
                List<String> encodings = Arrays.asList(accept_encodings);
                if (encodings.indexOf("gzip") == -1){
                    encodings.add("gzip");
                }
                cnt.setRequestProperty("Accept-Encoding", TextUtils.join(", ", encodings));
            }else {
                cnt.setRequestProperty("Accept-Encoding", "gzip");
            }
            cnt.setRequestProperty("Referer", referer);
            if (cookie != null){
                cnt.setRequestProperty("Cookie", cookie);
            }
            cnt.setRequestMethod("GET");
            if (redirect == null) {
                cnt.setInstanceFollowRedirects(true);
            }else {
                cnt.setInstanceFollowRedirects(redirect);
            }
            if (read_timeout == null) {
                cnt.setReadTimeout(9000);
            }else {
                cnt.setReadTimeout(read_timeout);
            }
            if (connect_timeout == null) {
                cnt.setConnectTimeout(2000);
            }else {
                cnt.setConnectTimeout(connect_timeout);
            }
            cnt.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-1);
        }
        try {
            resp_code = cnt.getResponseCode();
            if (redirect != null && !redirect && resp_code == 302){
                return new HttpConnectionAndCode(cnt, -7, "");
            }
            List<String> encodings = cnt.getHeaderFields().get("content-encoding");
            if (encodings != null && encodings.get(0).equals("gzip")){
                in = new InputStreamReader(new GZIPInputStream(cnt.getInputStream()));
            }else {
                in = new InputStreamReader(cnt.getInputStream());
            }
            StringBuilder response_builder = new StringBuilder();
            char read_char;
            while((read_char = (char)in.read()) != (char)-1){
                response_builder.append(read_char);
            }
            response = response_builder.toString();
            if (tail != null) {
                if (response.contains(tail)) {
                    response = response.substring(0, response.indexOf(tail) + tail.length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-5);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-2);
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
        if (success_resp_text != null){
            if (!response.contains(success_resp_text)){
                //if cookie_delimiter != null but no server cookie, set_cookie = ""
                //if no response, response = ""
                return new HttpConnectionAndCode(cnt, -6, response, set_cookie, resp_code);
            }
        }

        //do not disconnect, keep alive
        //if cookie_delimiter != null but no server cookie, set_cookie = ""
        //if no response, response = ""
        return new HttpConnectionAndCode(cnt, 0, response, set_cookie, resp_code);
    }
}

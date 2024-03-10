package top.yvyan.guettable.Http;

import static java.lang.Math.max;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import top.yvyan.guettable.util.SSLUtils;

public class Get {
    /**
     * @return - 0 GET success
     * - -1 cannot open url
     * - -2 cannot close input stream
     * - -5 cannot get response
     * - -6 response check fail
     * - -7 302
     */
    public static HttpConnectionAndCode get(@NonNull final String u,
                                            @Nullable final String[] params,
                                            @NonNull final String user_agent,
                                            @NonNull final String referer,
                                            @Nullable final String cookie,
                                            @Nullable final String tail,
                                            @Nullable final String cookie_delimiter,
                                            @Nullable final String success_resp_text,
                                            @Nullable final String[] accept_encodings,
                                            @Nullable final Boolean redirect,
                                            @Nullable final Integer connect_timeout,
                                            @Nullable final Integer read_timeout,
                                            @Nullable final String content_type) {
        return get(u, params, user_agent, referer, cookie, tail, cookie_delimiter, success_resp_text, accept_encodings, redirect, connect_timeout, read_timeout, content_type,null);
    }

    public static HttpConnectionAndCode get(@NonNull final String u,
                                            @Nullable final String[] params,
                                            @NonNull final String user_agent,
                                            @NonNull final String referer,
                                            @Nullable final String cookie,
                                            @Nullable final String tail,
                                            @Nullable final String cookie_delimiter,
                                            @Nullable final String success_resp_text,
                                            @Nullable final String[] accept_encodings,
                                            @Nullable final Boolean redirect,
                                            @Nullable final Integer connect_timeout,
                                            @Nullable final Integer read_timeout,
                                            @Nullable final String content_type,
                                            @Nullable final Map<String,String> headers) {
        URL url;
        HttpURLConnection cnt;
        InputStreamReader in;
        String response;
        int resp_code;
        try {
            StringBuilder u_bulider = new StringBuilder();
            u_bulider.append(u);
            if (params != null && params.length > 0) {
                u_bulider.append("?").append(TextUtils.join("&", params));
            }
            url = new URL(u_bulider.toString());
            cnt = (HttpURLConnection) url.openConnection();
            cnt.setDoInput(true);
            cnt.setRequestProperty("User-Agent", user_agent);
            if (accept_encodings != null && accept_encodings.length > 0) {
                List<String> encodings = Arrays.asList(accept_encodings);
                if (!encodings.contains("gzip")) {
                    encodings.add("gzip");
                }
                cnt.setRequestProperty("Accept-Encoding", TextUtils.join(", ", encodings));
            } else {
                cnt.setRequestProperty("Accept-Encoding", "gzip");
            }
            cnt.setRequestProperty("Referer", referer);
            if (cookie != null) {
                cnt.setRequestProperty("Cookie", cookie);
            }
            cnt.setRequestMethod("GET");
            if (redirect == null) {
                cnt.setInstanceFollowRedirects(true);
            } else {
                cnt.setInstanceFollowRedirects(redirect);
            }
            if (content_type != null) {
                cnt.setRequestProperty("Content-Type", content_type);
            }

            if(headers!=null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    cnt.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            if (read_timeout == null) {
                cnt.setReadTimeout(8000);
            } else {
                cnt.setReadTimeout(read_timeout);
            }
            if (connect_timeout == null) {
                cnt.setConnectTimeout(4000);
            } else {
                cnt.setConnectTimeout(connect_timeout);
            }
            if (cnt instanceof HttpsURLConnection) { // 判断是否为https请求
                SSLContext sslContext = SSLUtils.getSSLContextWithoutCer();
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                ((HttpsURLConnection) cnt).setSSLSocketFactory(sslSocketFactory);
                ((HttpsURLConnection) cnt).setHostnameVerifier(SSLUtils.hostnameVerifier);
            }
            cnt.connect();
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpConnectionAndCode(-1);
        }
        try {
            resp_code = cnt.getResponseCode();
            if (redirect != null && !redirect && (resp_code >= 300 && resp_code < 400)) {
                // 我们这里不考虑清除cookie的情况 (无视所有cookie参数)
                StringBuilder cookie_builder = new StringBuilder();
                List<String> cookies = cnt.getHeaderFields().get("Set-Cookie");
                if (cookies != null) {
                    for (String cookie_resp : cookies) {
                        cookie_builder.append(cookie_resp.substring(0, cookie_resp.indexOf(";") + 1) + " ");
                    }
                }
                return new HttpConnectionAndCode(cnt, -7, "",  cookie_builder.length() == 0 ? "" : cookie_builder.substring(0,  max(0,cookie_builder.length() - 2)), resp_code);
            }
            List<String> encodings = cnt.getHeaderFields().get("content-encoding");
            if (resp_code < 400) {
                if (encodings != null && encodings.get(0).equals("gzip")) {
                    in = new InputStreamReader(new GZIPInputStream(cnt.getInputStream()));
                } else {
                    in = new InputStreamReader(cnt.getInputStream());
                }
            } else {
                if (encodings != null && encodings.get(0).equals("gzip")) {
                    in = new InputStreamReader(new GZIPInputStream(cnt.getErrorStream()));
                } else {
                    in = new InputStreamReader(cnt.getErrorStream());
                }
            }
            StringBuilder response_builder = new StringBuilder();
            char[] buffer = new char[4 * 1024];
            for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
                response_builder.append(buffer, 0, numRead);
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
            StringBuilder cookie_builder = new StringBuilder();
            List<String> cookies = cnt.getHeaderFields().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie_resp : cookies) {
                    cookie_builder.append(cookie_resp.substring(0, cookie_resp.indexOf(";") + 1) + " ");
                }
            }
            set_cookie = cookie_builder.length() == 0 ? "" : cookie_builder.substring(0,  max(0,cookie_builder.length() - 2));
        }

        //do not disconnect, keep alive
        if (success_resp_text != null) {
            if (!response.contains(success_resp_text)) {
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

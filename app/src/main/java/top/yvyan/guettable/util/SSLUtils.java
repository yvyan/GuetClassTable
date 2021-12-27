package top.yvyan.guettable.util;

import android.annotation.SuppressLint;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {

    public static SSLContext getSSLContextWithoutCer() throws NoSuchAlgorithmException, KeyManagementException {
        // 实例化SSLContext
        // 这里参数可以用TSL 也可以用SSL
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{trustManagers}, new SecureRandom());
        return sslContext;
    }

    private static final TrustManager trustManagers = new X509TrustManager() {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    /**
     * 验证主机名
     */
    public static HostnameVerifier hostnameVerifier = (hostname, session) -> true;
}

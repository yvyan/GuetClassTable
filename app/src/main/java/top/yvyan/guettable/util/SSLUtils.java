package top.yvyan.guettable.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLUtils {
    public static SSLContext getSSLContextWithCer(Context context) throws NoSuchAlgorithmException, IOException, CertificateException,
            KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        // 实例化SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");

        // 从assets中加载证书
        // 在HTTPS通讯中最常用的是cer/crt和pem
        InputStream inStream = context.getAssets().open("lin.cer");

        /*
         * X.509 标准规定了证书可以包含什么信息，并说明了记录信息的方法 常见的X.509证书格式包括：
         * cer/crt是用于存放证书，它是2进制形式存放的，不含私钥。
         * pem跟crt/cer的区别是它以Ascii来表示，可以用于存放证书或私钥。
         */
        // 证书工厂
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        Certificate cer = cerFactory.generateCertificate(inStream);

        // 密钥库
        /*
         * Pkcs12也是证书格式 PKCS#12是“个人信息交换语法”。它可以用来将x.509的证书和证书对应的私钥打包，进行交换。
         */
        KeyStore keyStory = KeyStore.getInstance("PKCS12");
        // keyStory.load(inStream, "123456".toCharArray());
        keyStory.load(null, null);
        // 加载证书到密钥库中
        keyStory.setCertificateEntry("tsl", cer);

        // 密钥管理器
        KeyManagerFactory kMFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kMFactory.init(keyStory, null);
        // 信任管理器
        TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmFactory.init(keyStory);

        // 初始化sslContext
        sslContext.init(kMFactory.getKeyManagers(), tmFactory.getTrustManagers(), new SecureRandom());
        inStream.close();
        return sslContext;
    }

    public static SSLContext getSSLContextWithoutCer() throws NoSuchAlgorithmException, KeyManagementException {
        // 实例化SSLContext
        // 这里参数可以用TSL 也可以用SSL
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { trustManagers }, new SecureRandom());
        return sslContext;
    }

    private static TrustManager trustManagers = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    /**
     * 验证主机名
     */
    public static HostnameVerifier hostnameVerifier = (hostname, session) -> {
        return true;
    };
}

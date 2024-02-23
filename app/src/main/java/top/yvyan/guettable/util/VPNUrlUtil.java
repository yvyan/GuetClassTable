package top.yvyan.guettable.util;

import android.util.Log;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class VPNUrlUtil {
    public static final String vpnKey = "wrdvpnisthebest!";
    public static final String vpnIV = "wrdvpnisthebest!";
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getVPNUrl(String src, boolean isVPN) {
        if (!isVPN) return src;
        try {
            Log.d("url", src);
            URL url = new URL(src);
            if (url.getHost().equals("v.guet.edu.cn")) {
                return src;
            }
            byte[] key = vpnKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            IvParameterSpec Iv = new IvParameterSpec(vpnIV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, Iv);
            byte[] Encrypted = cipher.doFinal(url.getHost().getBytes(StandardCharsets.UTF_8));
            String HexResult = bytesToHex(vpnKey.getBytes(StandardCharsets.UTF_8)) + bytesToHex(Encrypted);
            String ret = "https://v.guet.edu.cn/" + url.getProtocol() + "/" + HexResult + (("".equals(url.getPath())||url.getPath()==null) ? "/":url.getPath()) + (url.getQuery()==null  ? "" : "?" + url.getQuery());
            Log.d("url2", ret);
            return ret;
        } catch (Exception e) {
            Log.e("a", e.toString());
        }
        return "";
    }

}

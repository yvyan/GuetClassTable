package top.yvyan.guettable.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {
    private static final String publicExponentStr = "010001";
    private static final String modulusStr = "00b5eeb166e069920e80bebd1fea4829d3d1f3216f2aabe79b6c47a3c18dcee5fd22c2e7ac519cab59198ece036dcf289ea8201e2a0b9ded307f8fb704136eaeb670286f5ad44e691005ba9ea5af04ada5367cd724b5a26fdb5120cc95b6431604bd219c6b7d83a6f8f24b43918ea988a76f93c333aa5a20991493d4eb1117e7b1";

    /**
     * CAS登录RSA公钥加密
     *
     * @param text 未加密字符串
     * @return     RSA加密后的字符串(16进制字符串)
     */
    public static String CASEncryption(String text) {
        try {
            //公钥加密
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger modulus = new BigInteger(modulusStr, 16);
            BigInteger publicExponent = new BigInteger(publicExponentStr, 16);
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
            PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptData = cipher.doFinal(text.getBytes());
            return byte2hex(encryptData);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * VPN登录RSA加密
     *
     * @param text 未加密字符串
     * @return     RSA加密后的字符串(16进制字符串)
     */
    public static String VPNEncryption(String text) {
        int[] key = {134, 8, 187, 0, 251, 59, 238, 74, 176, 180, 24, 67, 227, 252, 205, 80};
        int pwd_len = text.length();
        try {
            if (text.length() % 16 != 0) {
                int need_num = 16 - text.length() % 16;
                StringBuilder pwd_builder = new StringBuilder();
                pwd_builder.append(text);
                for (int i = 0; i < need_num; i++) {
                    pwd_builder.append("0");
                }
                text = pwd_builder.toString();
            }
            byte[] pwd_bytes = text.getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < pwd_bytes.length; i++) {
                pwd_bytes[i] ^= key[i % 16];
            }
            StringBuilder encrypt_builder = new StringBuilder();
            encrypt_builder.append("77726476706e6973617765736f6d6521");
            for (int i = 0; i < pwd_len; i++) {
                byte b = pwd_bytes[i];
                encrypt_builder.append(String.format("%02x", b));
            }
            return encrypt_builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static String byte2hex(byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xff);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString();
    }
}

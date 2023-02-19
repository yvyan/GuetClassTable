package top.yvyan.guettable.util;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

public class AESUtil {
    /**
     * CAS登录AES - CBC加密
     *
     * @param text 未加密字符串
     * @param skey AES CBC Key
     * @return AES加密后的字符串(base64 字符串)
     */
    public static String CASEncryption(String text,String skey)  {
        try {
            byte[] bkey = skey.getBytes("UTF-8");
            SecretKeySpec secretKey = new SecretKeySpec(bkey, "AES");
            IvParameterSpec Iv=new IvParameterSpec(AESUtil.getRandomString(16).getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,Iv);
            String paddingText = AESUtil.getRandomString(64)+text;
            return Base64.encode(cipher.doFinal(paddingText.getBytes("UTF-8")),Base64.DEFAULT).toString();
        } catch (Exception ignored) {

        }
        return null;
    }
    public static String getRandomString(int length) {
        String RNDCHARS = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        StringBuilder rndString = new StringBuilder();
        Random rnd = new Random();
        for (int i=0;i<length;i++) {
            int index = (int) (rnd.nextFloat() * 48);
            rndString.append(RNDCHARS.charAt(index));
        }
        return rndString.toString();
    }
}

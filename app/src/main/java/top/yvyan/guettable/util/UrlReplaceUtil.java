package top.yvyan.guettable.util;

public class UrlReplaceUtil {

    public static String getBkjwUrlByVPN(boolean isVPN, String url) {
        String bkjwPath = "https://bkjw.guet.edu.cn";
        String bkjwVPNPath = "https://v.guet.edu.cn/https/77726476706e69737468656265737421f2fc4b8b69377d556a468ca88d1b203b";
        if (isVPN) {
            url = bkjwVPNPath + url;
        } else {
            url = bkjwPath + url;
        }
        return url;
    }
}

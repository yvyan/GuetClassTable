package top.yvyan.guettable.util;

public class UrlReplaceUtil {

    /**
     * URL替换(使国院教务系统可以和通用教务系统共同使用)
     *
     * @param isInternational 是否为国院账号
     * @param url             URL后接内容(https://gyjxgl.guet.edu.cn/Login/SubmitLogin -> /Login/SubmitLogin)
     * @return                完整的URL(https://gyjxgl.guet.edu.cn/Login/SubmitLogin)
     */
    public static String getUrlByInternational(boolean isInternational, String url) {
        String path = "http://172.16.13.22";
        String internationalPath = "https://gyjxgl.guet.edu.cn";
        if (isInternational) {
            return internationalPath + url;
        } else {
            return path + url;
        }
    }

    public static String getUrlByVPN(boolean isVPN, String url) {
        String bkjwPath = "http://172.16.13.22";
        String internationalPath = "https://gyjxgl.guet.edu.cn";
        String bkjwVPNPath = "https://v.guet.edu.cn/http/77726476706e69737468656265737421a1a013d2766626012d46dbfe";
        String internationalBkjwVPNPath = "https://v.guet.edu.cn/https/77726476706e69737468656265737421f7ee4b84203c26576b0d9de29d51367b8932";
        if (url.contains(bkjwPath)) {
            if (isVPN) {
                url = url.replace(bkjwPath, bkjwVPNPath);
            }
        } else if (url.contains(internationalPath)) {
            if (isVPN) {
                url = url.replace(internationalPath, internationalBkjwVPNPath);
            }
        }
        return url;
    }
}

package top.yvyan.guettable.util;

public class UrlReplaceUtil {

    /**
     * URL替换(使国院教务系统可以和通用教务系统共同使用)
     *
     * @param isInternational 是否为国院账号
     * @param url             URL后接内容(https://gyjxgl.guet.edu.cn/Login/SubmitLogin -> /Login/SubmitLogin)
     * @return                完整的URL(https://gyjxgl.guet.edu.cn/Login/SubmitLogin)
     */
    public static String getUrl(boolean isInternational, String url) {
        String path = "http://172.16.13.22";
        String internationalPath = "https://gyjxgl.guet.edu.cn";
        if (isInternational) {
            return internationalPath + url;
        } else {
            return path + url;
        }
    }
}

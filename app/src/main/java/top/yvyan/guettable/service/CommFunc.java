package top.yvyan.guettable.service;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.yvyan.guettable.R;
import top.yvyan.guettable.activity.WebViewActivity;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.ToastUtil;
import top.yvyan.guettable.util.VPNUrlUtil;

public class CommFunc {

    /**
     * APP内打开链接
     *
     * @param activity activity
     * @param title    标题
     * @param url      URL
     * @param share    是否可以分享和浏览器打开
     */
    public static void openUrl(Activity activity, String title, String url, boolean share) {
        if (url == null || url.isEmpty()) {
            DialogUtil.showTextDialog(activity, "功能维护中！");
        }
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        MobclickAgent.onEventObject(activity, "openUrl", urlMap);
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_URL, url);
        intent.putExtra(WebViewActivity.WEB_SHARE, share);
        intent.putExtra(WebViewActivity.WEB_TITLE, title);
        activity.startActivity(intent);
    }

    /**
     * 使用浏览器打开链接
     *
     * @param activity activity
     * @param url      URL
     */
    public static void openBrowser(Activity activity, String url) {
        if (url == null || url.isEmpty()) {
            DialogUtil.showTextDialog(activity, "功能维护中！");
            return;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            Map<String, Object> urlMap = new HashMap<>();
            urlMap.put("url", url);
            MobclickAgent.onEventObject(activity, "openUrl", urlMap);
            Uri uri = Uri.parse(url);
            Intent webIntent = new Intent();
            webIntent.setAction("android.intent.action.VIEW");
            webIntent.setData(uri);
            activity.startActivity(webIntent);
        }
    }

    /**
     * 分享给同学
     */
    public static void shareText(Activity activity, String title, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        activity.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * 自动登录教务
     *
     * @param activity activity
     */
    public static void noLoginWebBKJW(Activity activity) {
        new Thread(() -> {
            final boolean[] noLogin = {false};
            TokenData tokenData = TokenData.newInstance(activity);
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEB_URL, VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn/Login/MainDesktop",TokenData.isVPN()));
            intent.putExtra(WebViewActivity.WEB_SHARE_URL, VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn/",TokenData.isVPN()));
            DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
                @Override
                public void onClickYes() {
                    AppUtil.reportFunc(activity, "登录教务-跳过");
                    activity.startActivity(intent);
                    noLogin[0] = true;
                }

                @Override
                public void onClickBack() {
                }
            };
            final AlertDialog[] dialog = new AlertDialog[1];
            activity.runOnUiThread(() -> dialog[0] = DialogUtil.setTextDialog(activity, "自动登录中...(最长需要15s)", "跳过", iDialogService, true));

            int LoginState = tokenData.refresh();
            if (LoginState == -3) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    ToastUtil.showToast(activity, "登录状态丢失，请输入验证码后稍后重试");
                });
                return;
            }
            if (!noLogin[0]) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                });
                intent.putExtra(WebViewActivity.WEB_URL, VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn/Login/MainDesktop",TokenData.isVPN()));
                intent.putExtra(WebViewActivity.WEB_SHARE_URL, VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn/",TokenData.isVPN()));
               // intent.putExtra(WebViewActivity.WEB_REFERER, VPNUrlUtil.getVPNUrl("https://bkjw.guet.edu.cn/",TokenData.isVPN()));
                intent.putExtra(WebViewActivity.WEB_COOKIE, tokenData.getBkjwCookie());
                AppUtil.reportFunc(activity, "登录教务-免登录");
                activity.startActivity(intent);
            }
        }).start();
    }

    /**
     * 自动登录教务
     *
     * @param activity activity
     */
    public static void noLoginWebBKJWTest(Activity activity) {
        new Thread(() -> {
            final boolean[] noLogin = {false};
            TokenData tokenData = TokenData.newInstance(activity);
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEB_URL, VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home",TokenData.isVPN()));
            intent.putExtra(WebViewActivity.WEB_SHARE_URL, VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home",TokenData.isVPN()));
            DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
                @Override
                public void onClickYes() {
                    AppUtil.reportFunc(activity, "登录教务-跳过");
                    activity.startActivity(intent);
                    noLogin[0] = true;
                }

                @Override
                public void onClickBack() {
                }
            };
            final AlertDialog[] dialog = new AlertDialog[1];
            activity.runOnUiThread(() -> dialog[0] = DialogUtil.setTextDialog(activity, "自动登录中...(最长需要15s)", "跳过", iDialogService, true));

            int LoginState = tokenData.refresh();
            if (LoginState == -3) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    ToastUtil.showToast(activity, "登录状态丢失，请输入验证码后稍后重试");
                });
                return;
            }
            if (!noLogin[0]) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                });
                intent.putExtra(WebViewActivity.WEB_URL, VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home",TokenData.isVPN()));
                intent.putExtra(WebViewActivity.WEB_SHARE_URL, VPNUrlUtil.getVPNUrl("https://bkjwtest.guet.edu.cn/student/home",TokenData.isVPN()));
                intent.putExtra(WebViewActivity.WEB_COOKIE, tokenData.getbkjwTestCookie());
                AppUtil.reportFunc(activity, "登录教务-免登录");
                activity.startActivity(intent);
            }
        }).start();
    }

    /**
     * 自动登录智慧校园
     *
     * @param activity activity
     */
    public static void noLoginWebICampus(Activity activity) {
        new Thread(() -> {
            final boolean[] noLogin = {false};
            TokenData tokenData = TokenData.newInstance(activity);
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEB_URL, activity.getResources().getString(R.string.url_smart_campus));
            intent.putExtra(WebViewActivity.WEB_SHARE_URL, activity.getResources().getString(R.string.url_smart_campus));
            DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
                @Override
                public void onClickYes() {
                    AppUtil.reportFunc(activity, "智慧校园-跳过");
                    activity.startActivity(intent);
                    noLogin[0] = true;
                }

                @Override
                public void onClickBack() {
                }
            };
            final AlertDialog[] dialog = new AlertDialog[1];
            activity.runOnUiThread(() -> dialog[0] = DialogUtil.setTextDialog(activity, "自动登录中...(最长需要15s)", "跳过", iDialogService, true));

            int LoginState = tokenData.refresh();
            if (LoginState == -3) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    ToastUtil.showToast(activity, "登录状态丢失，请输入验证码后稍后重试");
                });
                return;
            }

            if (TokenData.isVPN()) {
                tokenData.setVPNCASCookie();
            }
            if (!noLogin[0]) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                });
                intent.putExtra(WebViewActivity.WEB_URL, activity.getResources().getString(R.string.url_smart_campus));
                intent.putExtra(WebViewActivity.WEB_SHARE_URL, activity.getResources().getString(R.string.url_smart_campus));
                intent.putExtra(WebViewActivity.WEB_REFERER, activity.getResources().getString(R.string.url_smart_campus));
                //设置cookie
                if (TokenData.isVPN()) {
                    intent.putExtra(WebViewActivity.WEB_COOKIE_URL, activity.getResources().getString(R.string.url_vpn));
                    intent.putExtra(WebViewActivity.WEB_COOKIE, tokenData.getBkjwCookie());
                } else {
                    intent.putExtra(WebViewActivity.WEB_COOKIE_URL, activity.getResources().getString(R.string.url_Authserver));
                    intent.putExtra(WebViewActivity.WEB_COOKIE, tokenData.getCASCookie());
                }
                AppUtil.reportFunc(activity, "智慧校园-免登录");
                activity.startActivity(intent);
            }
        }).start();
    }

    /**
     * 自动登录VPN
     *
     * @param activity activity
     */
    public static void noLoginWebVPN(Activity activity) {
        new Thread(() -> {
            final boolean[] noLogin = {false};
            TokenData tokenData = TokenData.newInstance(activity);
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(WebViewActivity.WEB_URL, activity.getResources().getString(R.string.url_vpn));
            DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
                @Override
                public void onClickYes() {
                    AppUtil.reportFunc(activity, "登录VPN-跳过");
                    activity.startActivity(intent);
                    noLogin[0] = true;
                }

                @Override
                public void onClickBack() {
                }
            };
            final AlertDialog[] dialog = new AlertDialog[1];
            activity.runOnUiThread(() -> dialog[0] = DialogUtil.setTextDialog(activity, "自动建立连接中...(最长需要15s)", "跳过", iDialogService, true));
            int LoginState = tokenData.refresh();
            if (LoginState == -3) {
                activity.runOnUiThread(() -> {
                    if (dialog[0] != null && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                    }
                    ToastUtil.showToast(activity, "登录状态丢失，请输入验证码后稍后重试");
                });
                return;
            }
            String token = tokenData.getVpnToken();
            tokenData.setVPNCASCookie();
            if (!noLogin[0]) {
                activity.runOnUiThread(() -> {
                    dialog[0].dismiss();
                    WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                });
                intent.putExtra(WebViewActivity.WEB_REFERER, activity.getResources().getString(R.string.vpn_url));
                if (token != null) {
                    intent.putExtra(WebViewActivity.WEB_COOKIE, token);
                }
                AppUtil.reportFunc(activity, "登录VPN-免登录");
                activity.startActivity(intent);
            }
        }).start();
    }

    /**
     * 检查是否为校园网后使用浏览器打开
     *
     * @param activity activity
     * @param web      内网网址
     * @param vpnWeb   外网网址
     * @param hint     提示信息
     */
    public static void checkVpn(Activity activity, String web, String vpnWeb, String hint) {
        if (hint != null) {
            ToastUtil.showLongToast(activity, hint);
        }
        if (!TokenData.isVPN()) { //内网直接打开对应网址
            openBrowser(activity, web);
        } else { //外网登录vpn后打开对应网址
            openBrowser(activity, vpnWeb);
        }
    }

    /**
     * 自动登录VPN打开相应链接
     *
     * @param activity activity
     * @param web      内网网址
     * @param vpnWeb   外网网址
     */
    public static void noLoginWebVPN(Activity activity, String web, String vpnWeb) {
        TokenData tokenData = TokenData.newInstance(activity);
        if (!TokenData.isVPN()) { //内网直接打开对应网址
            openUrl(activity, null, web, true);
        } else { //外网登录vpn后打开对应网址
            new Thread(() -> {
                final boolean[] noLogin = {false};

                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra(WebViewActivity.WEB_URL, vpnWeb);
                DialogUtil.IDialogService iDialogService = new DialogUtil.IDialogService() {
                    @Override
                    public void onClickYes() {
                        AppUtil.reportFunc(activity, "登录VPN-跳过");
                        activity.startActivity(intent);
                        noLogin[0] = true;
                    }

                    @Override
                    public void onClickBack() {
                    }
                };
                final AlertDialog[] dialog = new AlertDialog[1];
                activity.runOnUiThread(() -> dialog[0] = DialogUtil.setTextDialog(activity, "自动建立连接中...(最长需要15s)", "跳过", iDialogService, true));
                int LoginState = tokenData.refresh();
                if (LoginState == -3) {
                    activity.runOnUiThread(() -> {
                        if (dialog[0] != null && dialog[0].isShowing()) {
                            dialog[0].dismiss();
                        }
                        ToastUtil.showToast(activity, "登录状态丢失，请输入验证码后稍后重试");
                    });
                    return;
                }
                String token = tokenData.getVpnToken();
                tokenData.setVPNCASCookie();
                if (!noLogin[0]) {
                    activity.runOnUiThread(() -> {
                        dialog[0].dismiss();
                        WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                    });
                    intent.putExtra(WebViewActivity.WEB_REFERER, "https://v.guet.edu.cn/login");
                    if (token != null) {
                        intent.putExtra(WebViewActivity.WEB_COOKIE, token);
                    }
                    AppUtil.reportFunc(activity, "登录VPN-免登录");
                    activity.startActivity(intent);
                }
            }).start();
        }
    }
}

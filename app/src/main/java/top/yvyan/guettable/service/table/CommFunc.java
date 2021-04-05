package top.yvyan.guettable.service.table;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.yvyan.guettable.activity.WebViewActivity;
import top.yvyan.guettable.data.GeneralData;
import top.yvyan.guettable.data.TokenData;
import top.yvyan.guettable.util.AppUtil;
import top.yvyan.guettable.util.DialogUtil;
import top.yvyan.guettable.util.UrlReplaceUtil;

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
        }
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        MobclickAgent.onEventObject(activity, "openUrl", urlMap);
        Uri uri = Uri.parse(url);
        Intent webIntent = new Intent();
        webIntent.setAction("android.intent.action.VIEW");
        webIntent.setData(uri);
        activity.startActivity(webIntent);
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
            intent.putExtra(WebViewActivity.WEB_URL, UrlReplaceUtil.getUrlByVPN(TokenData.isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(activity).isInternational(), "/Login/MainDesktop")));
            intent.putExtra(WebViewActivity.WEB_SHARE_URL, UrlReplaceUtil.getUrlByVPN(TokenData.isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(activity).isInternational(), "/")));
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
            activity.runOnUiThread(() -> dialog[0] = DialogUtil.showProgress(activity, "自动登录中...(最长需要20s)", "跳过", iDialogService));

            tokenData.refresh();
            if (!noLogin[0]) {
                activity.runOnUiThread(() -> {
                    dialog[0].dismiss();
                    WebViewActivity.cleanCash(Objects.requireNonNull(activity));
                });
                intent.putExtra(WebViewActivity.WEB_URL, UrlReplaceUtil.getUrlByVPN(TokenData.isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(activity).isInternational(), "/Login/MainDesktop")));
                intent.putExtra(WebViewActivity.WEB_SHARE_URL, UrlReplaceUtil.getUrlByVPN(TokenData.isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(activity).isInternational(), "/")));
                intent.putExtra(WebViewActivity.WEB_REFERER, UrlReplaceUtil.getUrlByVPN(TokenData.isVPN, UrlReplaceUtil.getUrlByInternational(GeneralData.newInstance(activity).isInternational(), "/")));
                intent.putExtra(WebViewActivity.WEB_COOKIE, tokenData.getCookie());
                AppUtil.reportFunc(activity, "登录教务-免登录");
                activity.startActivity(intent);
            }
        }).start();
    }
}

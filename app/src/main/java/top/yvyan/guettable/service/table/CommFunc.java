package top.yvyan.guettable.service.table;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.activity.WebViewActivity;
import top.yvyan.guettable.util.DialogUtil;

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
}

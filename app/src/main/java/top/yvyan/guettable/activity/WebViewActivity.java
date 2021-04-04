package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.table.CommFunc;
import top.yvyan.guettable.util.BackgroundUtil;

public class WebViewActivity extends AppCompatActivity {
    public static String WEB_URL = "webURL";
    public static String WEB_TITLE = "webTitle";
    public static String WEB_COOKIE = "webCookie";
    public static String WEB_REFERER = "webReferer";
    public static String WEB_SHARE = "webShare";
    public static String WEB_SHARE_URL = "webShareText";

    private WebView webView;
    private ProgressBar progressBar;
    private TextView webTitle;
    private ImageView moreButton;

    String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BackgroundUtil.setPageTheme(this, SingleSettingData.newInstance(getApplicationContext()).getThemeId());
        setContentView(R.layout.activity_web_view);
        BackgroundUtil.setFullAlphaStatus(this);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        progressBar = findViewById(R.id.progressbar);//进度条
        webView = findViewById(R.id.webView);
        webTitle = findViewById(R.id.title);
        Intent intent = getIntent();
        final String url = intent.getStringExtra(WEB_URL);
        String title = intent.getStringExtra(WEB_TITLE);
        String cookie = intent.getStringExtra(WEB_COOKIE);
        String referer = intent.getStringExtra(WEB_REFERER);
        boolean share = intent.getBooleanExtra(WEB_SHARE, true);
        shareUrl = intent.getStringExtra(WEB_SHARE_URL);
        if (shareUrl == null) {
            shareUrl = url;
        }
        if (cookie != null) {
            setCookies(url, cookie);
        }
        if (title == null) {
            title = "正在加载...";
        }
        webTitle.setText(title);
        if (referer != null) {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", referer);
            webView.loadUrl(url, header);
        } else {
            webView.loadUrl(url);
        }
        Log.d("1586", "cookie:" + cookie + "; url:" + url + "; referer:" + referer);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        if (share) {
            moreButton = findViewById(R.id.more);
            moreButton.setVisibility(View.VISIBLE);
            moreButton.setOnClickListener(view -> showPopMenu());
        }
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private final WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            CookieManager cookieManager = CookieManager.getInstance();

            Log.e("1586", request.getUrl() + "; " + request.getRequestHeaders() + "; " + cookieManager.getCookie(request.getUrl().toString()));
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

    };

    private final WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (title.length() > 15) {
                title = title.substring(0, 14);
                title += "...";
            }
            webTitle.setText(title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    /**
     * 点击返回按钮
     *
     * @param view view
     */
    public void doBack(View view) {
        finish();
    }

    /**
     * 获取URL的域名
     */
    private String getDomain(String url) {
        url = url.replace("http://", "").replace("https://", "");
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf('/'));
        }
        return url;
    }

    /**
     * 设置cookie
     *
     * @param url    URL
     * @param cookie Cookie
     */
    public void setCookies(String url, String cookie) {
        if (!TextUtils.isEmpty(cookie)) {
            String[] cookieArray = cookie.split(";"); //多个Cookie是使用分号分隔的
            for (String s : cookieArray) {
                int position = s.indexOf("="); //在Cookie中键值使用等号分隔
                String cookieName = s.substring(0, position);   //获取键
                String cookieValue = s.substring(position + 1); //获取值

                String value = cookieName + "=" + cookieValue;  //键值对拼接成 value
                CookieManager.getInstance().setCookie(getDomain(url), value); //设置 Cookie
            }
        }
    }

    /**
     * 显示弹出菜单
     */
    public void showPopMenu() {
        PopupMenu popup = new PopupMenu(this, moreButton);
        popup.getMenuInflater().inflate(R.menu.web_popmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.web_top1) {
                //浏览器打开
                CommFunc.openBrowser(this, shareUrl);
            } else {
                //分享
                CommFunc.shareText(this, "分享链接", "我通过“桂电课程表”分享给你链接:" + shareUrl);
            }
            return true;
        });
        popup.show();
    }
}
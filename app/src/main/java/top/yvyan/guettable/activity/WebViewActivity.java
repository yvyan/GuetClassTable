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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.R;

public class WebViewActivity extends AppCompatActivity {
    public static String WEB_URL = "webURL";
    public static String WEB_TITLE = "webTitle";
    public static String WEB_COOKIE = "webCookie";
    public static String WEB_REFERER = "webReferer";

    private WebView webView;
    private ProgressBar progressBar;
    TextView webTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progressBar = findViewById(R.id.progressbar);//进度条

        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        Intent intent = getIntent();
        final String url = intent.getStringExtra(WEB_URL);
        String title = intent.getStringExtra(WEB_TITLE);
        String cookie = intent.getStringExtra(WEB_COOKIE);
        String referer = intent.getStringExtra(WEB_REFERER);
        if (cookie != null) {
            setCookies(url, cookie);
        }
        if (title == null) {
            title = "正在加载...";
        }
        Log.d("1586", "cookie:" + cookie + "; url:" + url + "; referer:" + referer);
        webTitle = findViewById(R.id.title);
        webTitle.setText(title);
        webView = findViewById(R.id.webView);
        if (referer != null) {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", referer);
            webView.loadUrl(url, header);
        } else {
            webView.loadUrl(url);
        }

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

//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            CookieManager cookieManager = CookieManager.getInstance();
//
//            Log.e("1586", request.getUrl() + "; " + request.getRequestHeaders() + "; " + cookieManager.getCookie(request.getUrl().toString()));
//            return super.shouldInterceptRequest(view, request);
//        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private final WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
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

    public void setCookies(String url, String cookie) {
        if (!TextUtils.isEmpty(cookie)) {
            String[] cookieArray = cookie.split(";");// 多个Cookie是使用分号分隔的
            for (String s : cookieArray) {
                int position = s.indexOf("=");// 在Cookie中键值使用等号分隔
                String cookieName = s.substring(0, position);// 获取键
                String cookieValue = s.substring(position + 1);// 获取值

                String value = cookieName + "=" + cookieValue;// 键值对拼接成 value
                Log.i("cookie", value);
                CookieManager.getInstance().setCookie(getDomain(url), value);// 设置 Cookie
            }
        }
    }
}
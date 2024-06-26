package top.yvyan.guettable.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.HashMap;
import java.util.Map;

import top.yvyan.guettable.R;
import top.yvyan.guettable.data.SingleSettingData;
import top.yvyan.guettable.service.CommFunc;
import top.yvyan.guettable.util.BackgroundUtil;

public class WebViewActivity extends AppCompatActivity {
    public static String WEB_URL = "webURL";
    public static String WEB_TITLE = "webTitle";
    public static String WEB_COOKIE = "webCookie";
    public static String WEB_COOKIE_URL = "webCookieUrl";
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
        String cookieUrl = intent.getStringExtra(WEB_COOKIE_URL);
        String referer = intent.getStringExtra(WEB_REFERER);
        boolean share = intent.getBooleanExtra(WEB_SHARE, true);
        shareUrl = intent.getStringExtra(WEB_SHARE_URL);
        if (shareUrl == null) {
            shareUrl = url;
        }
        if (cookie != null) {
            if (cookieUrl == null) {
                setCookies(url, cookie);
            } else {
                setCookies(cookieUrl, cookie);
            }
        }
        if (title == null) {
            title = "正在加载...";
        }
        webTitle.setText(title);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) { //显示图片
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.setDownloadListener((s, s1, s2, s3, l) -> { //实现下载
            Uri uri = Uri.parse(s);
            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent1);
        });
        WebSettings settings = webView.getSettings();
        settings.setBlockNetworkImage(false); //显示图片
        settings.setUseWideViewPort(true);    //设定支持viewport
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        if (referer != null) {
            Map<String, String> header = new HashMap<>();
            header.put("Referer", referer);
            webView.loadUrl(url, header);
        } else {
            webView.loadUrl(url);
        }
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                return true;
            } else if (url.startsWith("mqq")) { //仅允许QQ跳转
                try {
                    startActivity(Intent.parseUri(url, Intent.URI_INTENT_SCHEME));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            }
            return false;
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    private final WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            MaterialDialog.Builder localBuilder = new MaterialDialog.Builder(webView.getContext());
            localBuilder
                    .content(message)
                    .positiveText("确定")
                    .onPositive((dialog, which) -> {
                        result.confirm();
                    })
                    .cancelable(false)
                    .show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
            MaterialDialog.Builder localBuilder = new MaterialDialog.Builder(webView.getContext());
            localBuilder
                    .content(message)
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive((dialog, which) -> {
                        result.confirm();
                    })
                    .onNegative((dialog,which)->{
                        result.cancel();
                    })
                    .cancelable(false)
                    .show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, JsPromptResult result) {
            MaterialDialog.Builder localBuilder = new MaterialDialog.Builder(webView.getContext());
            localBuilder
                    .title(message)
                    .input("",defaultValue,(dialog, input)->{})
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive((dialog, which) -> {
                        result.confirm(dialog.getInputEditText().getText().toString());
                    })
                    .onNegative((dialog,which)->{
                        result.cancel();
                    })
                    .cancelable(false)
                    .show();
            return true;
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            MaterialDialog.Builder localBuilder = new MaterialDialog.Builder(webView.getContext());
            localBuilder.title("离开此页面").content(message)
                    .positiveText("离开")
                    .onPositive((dialog, which) -> {
                        result.confirm();
                    })
                    .cancelable(false)
                    .show();
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
                if (position != -1) {
                    String cookieName = s.substring(0, position);   //获取键
                    String cookieValue = s.substring(position + 1); //获取值

                    String value = cookieName + "=" + cookieValue;  //键值对拼接成 value
                    CookieManager.getInstance().setCookie(getDomain(url), value); //设置 Cookie
                }
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

    /**
     * 清除WebView数据
     *
     * @param context context
     */
    public static void cleanCash(Context context) {
        context.deleteDatabase("webview.db");
        context.deleteDatabase("webviewCache.db");
        WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage
        //清除cookie
        CookieManager.getInstance().removeAllCookies(aBoolean -> {
        });
    }
}
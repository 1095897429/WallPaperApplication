package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.定位 -- ok
 * 2.选择框 -- ok
 *  1.权限 SYSTEM_ALERT_WINDOW  || SYSTEM_OVERLAY_WINDOW
 */

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;


    @BindView(R.id.move_back)
    RelativeLayout move_back;


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    WebSettings webSettings;
    String loadUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {
        loadUrl = getIntent().getExtras().getString("loadUrl");
        setSetting();
        initClient();
        initChromeClient();
        //TODO 测试
//        loadUrl = "http://sle.semzyzh.com:8070/dc/redirect?m=3493894f";
        KLog.d("webView的加载url: " ,loadUrl);
        webview.loadUrl(loadUrl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(webview != null) {
            webview.stopLoading();
            webview.clearHistory();
            webview.destroy();
        }
    }

    private void initChromeClient() {
        //获取网页进度
        webview.setWebChromeClient(new WebChromeClient() {

            /** 定位回调 */
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mProgressBar.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(view, newProgress);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }


        });
    }


    private void initClient() {
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//在这里设置对应的操作
                return true;// false 可以解决由于重定向导致的webview.goback()无法返回的问题
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {

                handler.proceed();// 接受所有网站的证书
            }

        });
    }

    /**
     * webview 默认设置
     */
    private void setSetting() {
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js -- ok
        webSettings.setSupportZoom(true); //支持屏幕缩放
        //设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        webview.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webview.getSettings().setBlockNetworkImage(false); // 解决图片不显示
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);//添加对H5的支持  -- getItem' of null

        //其他细节操作 定位一些设置
        webSettings.setDatabaseEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);


        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式 有广告不可设置编码格式


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为,
            // 在这种模式下,WebView将允许一个安全的起源从其他来源加载内容，即使那是不安全的.
            // 如果app需要安全性比较高，不应该设置此模式
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决app中部分页面非https导致的问题
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KLog.i("ansen", "是否有上一个页面:" + webview.canGoBack());
//        if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
//            webview.goBack(); // goBack()表示返回webView的上一页面
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.move_back)
    public void MoveBack() {
        finish();
    }


}
package com.test.webjs;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private Button btn1, btn2, btn3, btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient() {
            /**
             * Android 通过WebViewClient的回调方法shouldOverrideUrlLoading()拦截URl
             * 解析Url协议，如果检测到是预先约定好的协议，就调用相应方法
             * @param view
             * @param request
             * @return
             */
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i(">>>", "overrideUrlLoading");
                //根据协议的参数，判断是否是需要的url
                //一般根据scheme(协议格式)&authority(协议名)判断(前两个参数)
                Uri uri = request.getUrl();
                //如果url的协议 = 预订好的js协议，就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    //如果authority = 预先约定协议里的webView，符合约定的协议拦截URl往下执行
                    if (uri.getAuthority().equals("webview")) {
                        //在此处进行调用安卓方法
                        Log.i(">>>", "js调用了Android的方法");
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);

            }

            /**
             * 开始加载页面时调用
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(">>>", "onPageStarted");
            }

            /**
             * 加载完成后调用
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(">>>", "onPageFinished");
            }

            /**
             * 加载页面资源时调用，每个资源的加载都会调用一次
             */
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.i(">>>", "onLoadResource");
            }

            /**
             * App里面使用webview控件的时候遇到了诸如404这类的错误的时候，
             * 若也显示浏览器里面的那种错误提示页面就显得很丑陋了，
             * 那么这个时候我们的app就需要加载一个本地的错误提示页面，即webview如何加载一个本地的页面
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i(">>>", "onReceivedError");
            }

            /**
             * webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
             * @param view
             * @param handler
             * @param error
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.i(">>>", "onReceivedSslError");
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webViewTools();
        webChromeClient();
        // webView.addJavascriptInterface(new AndroidToJs(),"test");
    }

    private void loadMethods(int caseLoad) {
        switch (caseLoad) {
            case 1:
                //1.加载一个网页
                webView.loadUrl("http://www.baidu.com");
                break;
            case 2:
                //2.加载apk包中html文件
                webView.loadUrl("file:///android_asset/test.html");
                break;
            case 3:
                //3.加载手机本地的html页面
                webView.loadUrl("content://com.com.test.webjs/sdcard/test.html");
                break;
            case 4:
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:callJS()");
                    }
                });
                break;
        }

    }

    /**
     * @param data     需要截取显示的内容 内容里不能出现
     *                 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
     * @param mimType  展示内容的类型
     * @param encoding 字节码
     */
    //4.加载HTML页面的一小段代码
    private void loadCodeBlock(String data, String mimType, String encoding) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                loadMethods(1);
                break;
            case R.id.btn2:
                loadMethods(2);
                break;
            case R.id.btn3:
                //   loadMethods(3);
                AndroidLoadJs("javascript:callJS()");
                break;
            case R.id.btn4:
                //    loadCodeBlock("", "", "");
                loadMethods(4);
                break;
        }
    }

    /**
     * webView的各个状态
     */
    private void webViewState() {
        //激活webView为活跃状态，能正常执行网页的响应
        webView.onResume();
        //当前页面失去焦点被切换到后台不可见状态，通过onPause动作通知内核暂停所有活动，比如Dom的解析，plugin的执行，javaScript的执行
        webView.onResume();
        //暂停所有webView的layout,parsing,降低Cup功耗
        webView.pauseTimers();
        //恢复pauseTimers状态
        webView.resumeTimers();
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        //rootLayout.removeView(webView);
        webView.destroy();
    }

    /**
     * 关于前进、后退网页
     */
    private void goOrBack() {
        //是否可以后退
        webView.canGoBack();
        //后退网页
        webView.goBack();
        //是否可以前进
        webView.canGoForward();
        //前进网页
        webView.goForward();
        //以当前的index为起始点前进或者后退到历史记录中指定的step
        //如果steps为负数则后退，正数则前进
        webView.goBackOrForward(1);

    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序
        webView.clearCache(true);
        //清除当前webView访问的历史记录
        //只会webView访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory();
        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData();
    }

    /**
     * webSetting类
     */
    private void webViewTools() {
        WebSettings webSettings = webView.getSettings();
        //如果webView与javaScript交换，则设为true
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setJavaScriptEnabled(true);
        //设置屏幕适配
        webSettings.setUseWideViewPort(true); //将图片调整到适合webView的大小
        webSettings.setLoadWithOverviewMode(true); //缩放至屏幕大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件，若为false，则该webView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他操作
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webView中的缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true); //开启DOM storage API 功能
        webSettings.setDatabaseEnabled(true);//开启database storage API 功能
        webSettings.setAppCacheEnabled(true); //开启application cache 功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache_path";
        webSettings.setAppCachePath(cacheDirPath);
    }

    /**
     * 由于设置了弹窗检验调用结果，所以需要支持js对话框
     * webView只是载体，内容渲染需要使用webViewChromeClient类去实现
     * 通过设置webChromeClient对象处理JavaScript的对话框
     * 设置响应js的Alert（）函数
     */
    private void webChromeClient() {

        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 获取网页的加载进度
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i(">>>", "Chrome:progress");
            }

            /**
             * 获取Web页面中的标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(">>>", "Chrome:receivedTitle");
            }

            /**
             * 重写Alert方法
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.i(">>>", "Chrome:JsAlert");
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("alert");
                adb.setMessage(message);
                adb.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                adb.setCancelable(true);
                adb.create().show();
                return true;
            }

            /**
             * 支持javascript的确认框
             * @param view
             * @param url
             * @param message
             * @param result
             * @return
             */
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.i(">>>", "confirm");
                return super.onJsConfirm(view, url, message, result);
            }

            /**
             * prompt:提示，引起，敏捷的，迅速的，立刻，提示
             * 支持javascript输入框
             * @param view
             * @param url
             * @param message
             * @param defaultValue
             * @param result
             * @return
             */
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.i(">>>", "confirm");
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

    }

    private void designWebView() {
//        View layoutView = new LinearLayout(this);
//        LinearLayout.LayoutParams linearLayout = new LinearLayout(-1,-1);
//        webView = new WebView(getApplicationContext());
//        webView.setLayoutParams(linearLayout);
//        webView.addView(webView);
    }

    private void webViewDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    private void AndroidLoadJs(String jsCode) {
        //
        /**
         * loadUrl 和 evaluateJavaScript 两种方法加载
         * loadUrl无返回值，evaluateJavascript 效率相对较高
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //evaluate:评估，认为，求...的值
            webView.evaluateJavascript(jsCode, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i(">>>", "jsCode>" + value);
                }
            });
        } else {
            webView.loadUrl(jsCode);
        }

    }

    private void JsLoadAndroid() {
        //1. webView的addJavascriptInterface()进行对象映射;
        //2.通过webViewClient的shouldOverrideUrlLoading()
        //3.通过webViewClient的onJsAlert(),onJsConfirm(),onJsPrompt()方法回调拦截JS对话框alert（），confirm（），prompt()消息
    }


}

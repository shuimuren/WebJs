package com.test.webjs;

import android.content.SyncStatusObserver;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Create by xmd on 2018/6/7
 * Describe:
 */
public class AndroidToJs {
    @JavascriptInterface
    public void hello(String msg){
        Log.i(">>>","Js调用了Android的方法>"+msg);
    }
}

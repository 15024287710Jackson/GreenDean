package com.example.greendean;

import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;

/**
 * 使用android-async-http-1.4.9包封装的异步HTTP请求工具类
 * Created by Administrator on 2016/11/30.
 */
public class AsyncHttpHelper {


    private static final AsyncHttpClient client;
    /**
     * android 客户端信息
     */
    private static String appUserAgent;

    static {

        client = new AsyncHttpClient();

        client.setMaxRetriesAndTimeout(3, 10000);
        client.setUserAgent(getUserAgent());
    }

    private static String getUserAgent() {
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder sb = new StringBuilder("jackie");
            sb.append("|Android");
            sb.append("|" + Build.VERSION.RELEASE);//手机系统版本
            sb.append("|" + Build.MODEL);//手机型号
            appUserAgent = sb.toString();
        }
        return appUserAgent;
    }

    private AsyncHttpHelper() {
    }

    public static AsyncHttpClient getClient() {
        return client;
    }
}

package com.a4nesia.baso.smartaccess;

import android.app.Application;

import android.content.Context;

public class MyApp extends Application {
    private static Context mContext;
    public static final String BASE_URL = "http://192.168.1.31/api/mobile/";
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}

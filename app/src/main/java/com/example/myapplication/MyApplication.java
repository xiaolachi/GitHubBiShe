package com.example.myapplication;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    public static MyApplication getApp() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}

package com.example.myapplication.utils;

import android.widget.Toast;

import com.example.myapplication.MyApplication;

public class UIutils {

    private final static UIutils sInstance = new UIutils();

    private UIutils() {

    }

    public static UIutils instance() {
        return sInstance;
    }

    public void toast(String str) {
        Toast.makeText(MyApplication.getApp().getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }
}

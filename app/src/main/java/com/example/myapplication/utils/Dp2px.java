package com.example.myapplication.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Dp2px {

    public static float dp2px(Context context, float dp){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        float density = displayMetrics.density;
        float v = density * 2;
        return v;

    }
    public static float px2dp(Context context,float dp){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        float density = displayMetrics.density;
        float v = density / 2;
        return v;

    }
}

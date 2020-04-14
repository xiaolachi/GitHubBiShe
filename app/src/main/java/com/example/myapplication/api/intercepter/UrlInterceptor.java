package com.example.myapplication.api.intercepter;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UrlInterceptor implements Interceptor {

    private Context mContext;

    public UrlInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i("lyf", "path:" + request.url());
        return chain.proceed(request);
    }
}

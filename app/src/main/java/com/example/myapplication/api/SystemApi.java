package com.example.myapplication.api;

import android.content.Context;

import com.example.myapplication.api.intercepter.UrlInterceptor;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SystemApi {

    private String DOMAIN = "http://192.168.0.107:8080/";
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private LoginApi mLoginApi;
    private MessageApi mMessageApi;
    private ScoreApi mScoreApi;
    private AnnounceApi mAnnounceApi;
    private ScholarApi mScholarApi;

    public SystemApi(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new UrlInterceptor(context))
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DOMAIN)
                .build();

        mLoginApi = mRetrofit.create(LoginApi.class);
        mMessageApi = mRetrofit.create(MessageApi.class);
        mScoreApi = mRetrofit.create(ScoreApi.class);
        mAnnounceApi = mRetrofit.create(AnnounceApi.class);
    }

    public Call<ResponseBody> login(RequestBody body) {
        return mLoginApi.login(body);
    }

    public Call<ResponseBody> getMessageList(String page, String pageSize) {
        return mMessageApi.getMessageList(page, pageSize);
    }

    public Call<ResponseBody> getScoreList(String page, String pagesize) {
        return mScoreApi.getScoreList(page, pagesize);
    }

    public Call<ResponseBody> getAnnounceList(String page, String pagesize) {
        return mAnnounceApi.getAnnounceList(page, pagesize);
    }

    public Call<ResponseBody> getScholarList(String page, String pagesize) {
        return mScoreApi.getScoreList(page, pagesize);
    }
}

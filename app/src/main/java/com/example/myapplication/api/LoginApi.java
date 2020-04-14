package com.example.myapplication.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("BiSheServer/main?action=login")
    Call<ResponseBody> login(@Body RequestBody body);
}

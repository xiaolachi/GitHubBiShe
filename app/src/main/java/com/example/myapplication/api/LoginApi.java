package com.example.myapplication.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    @POST("BiSheServer/main?action=login")
    Call<ResponseBody> login(@Body RequestBody body);

    @GET("BiSheServer/main?action=updatepwd")
    Call<ResponseBody> updatePassWord(@Query("utype") String type, @Query("oldpassword") String oldPwd, @Query("uaccount") String uaccount, @Query("newpassword") String newPwd);
}

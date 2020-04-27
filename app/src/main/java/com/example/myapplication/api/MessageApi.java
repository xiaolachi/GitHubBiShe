package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessageApi {

    @GET("BiSheServer/main?action=selectalluser")
    Call<ResponseBody> getMessageList(@Query("page") String page, @Query("pagesize") String pagesize);

    @GET("BiSheServer/main?action=adduser")
    Call<ResponseBody> addStudent(@Query("utype") String utype, @Query("upass") String pwd, @Query("uaccount") String stuAccount, @Query("stusex") String gender, @Query("name") String name, @Query("stusystem") String system, @Query("stuclass") String stuClass, @Query("classnumber") String classNum, @Query("stuidcard") String stuCard, @Query("stuscore") String credit);

    @GET("BiSheServer/main?action=selectClass")
    Call<ResponseBody> getSystemClass();

    @GET("BiSheServer/main?action=deluser")
    Call<ResponseBody> delStudent(@Query("utype") String utype, @Query("id") int id);

    @GET("BiSheServer/main?action=updatestuinfo")
    Call<ResponseBody> editAnnounce
            (@Query("stuaccount") String stuAccount,
             @Query("stusex") String gender,
             @Query("name") String name,
             @Query("stusystem") String system,
             @Query("stuclass") String stuClass,
             @Query("classnumber") String classNum,
             @Query("stuidcard") String stuCard,
             @Query("stuscore") String credit);

    @GET("BiSheServer/main?action=selectastu")
    Call<ResponseBody> lookUpMessage(@Query("uaccount") String uaccount);
}

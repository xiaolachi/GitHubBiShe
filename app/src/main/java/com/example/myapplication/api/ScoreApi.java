package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScoreApi {

    @GET("BiSheServer/main?action=selectscoreallstu")
    Call<ResponseBody> getScoreList(@Query("page") String page, @Query("pagesize") String pagesize);

    @GET("BiSheServer/main?action=addScoreStu")
    Call<ResponseBody> addStuScore(@Query("scorelist") String json);

    @GET("BiSheServer/main?action=selectscoreastu")
    Call<ResponseBody> lookUpScore(@Query("uaccount") String uaccount);

    @GET("BiSheServer/main?action=selectThirdStuList")
    Call<ResponseBody> getScoreGradeList();
}

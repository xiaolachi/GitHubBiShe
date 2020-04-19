package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScoreApi {

    @GET("BiSheServer/main?action=selectscoreallstu")
    Call<ResponseBody> getScoreList(@Query("page") String page, @Query("pagesize") String pagesize);

    @GET("")
    Call<ResponseBody> addStuScore();
}

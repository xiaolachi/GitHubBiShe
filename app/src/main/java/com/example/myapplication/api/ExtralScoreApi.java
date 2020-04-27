package com.example.myapplication.api;

import android.text.GetChars;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExtralScoreApi {

    @GET("BiSheServer/main?action=submitExtralScore")
    Call<ResponseBody> submitExtral(@Query("stu_account") String stu_account, @Query("awards_name") String stuAward, @Query("match_time") String matchDate, @Query("awards_grade") String etGrade, @Query("extral_score") String extralScore);

    @GET("BiSheServer/main?action=selectExtralScoreList")
    Call<ResponseBody> getExtralScoreList(@Query("page") String page, @Query("pagesize") String pageSize);

    @GET("BiSheServer/main?action=lookExtralScore")
    Call<ResponseBody> postExtalScore(@Query("id") String id, @Query("extralscore") String extralscore, @Query("islook") String islook);
}

package com.example.myapplication.api;

import android.widget.EditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AnnounceApi {

    @GET("BiSheServer/main?action=selectannounlist")
    Call<ResponseBody> getAnnounceList(@Query("page") String page, @Query("pagesize") String pagesize);

    @GET("BiSheServer/main?action=selectAnnonYears")
    Call<ResponseBody> getAnnonYears();

    @GET("BiSheServer/main?action=addScholarAnnounce")
    Call<ResponseBody> addAnnounce(@Query("content") String content, @Query("years") String years);

    @GET("BiSheServer/main?action=delScholarAnnounce")
    Call<ResponseBody> delAnnounce(@Query("id") String id);

    @GET("BiSheServer/main?action=editScholarAnnounce")
    Call<ResponseBody> editScholarAnnounce(@Query("id") String id, @Query("years") String selectType, @Query("content") String content);

    @GET("BiSheServer/main?action=selectAnnounceByCondition")
    Call<ResponseBody> lookUpAnnounce(@Query("condition") String contentKey);
}
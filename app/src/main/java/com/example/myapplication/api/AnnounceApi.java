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
    Call<ResponseBody> delAnnounce(@Query("years") String years);
}
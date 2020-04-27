package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScholarApi {

    @GET("BiSheServer/main?action=selectscholarlist")
    Call<ResponseBody> getScholarList(@Query("page") String page, @Query("pagesize") String pagesize);

    @GET("BiSheServer/main?action=addscholarlist")
    Call<ResponseBody> addScholar(@Query("scholarlist") String json);
}

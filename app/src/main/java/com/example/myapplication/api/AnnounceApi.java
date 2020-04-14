package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AnnounceApi {

    @GET("BiSheServer/main?action=selectannounlist")
    Call<ResponseBody> getAnnounceList(@Query("page") String page, @Query("pagesize") String pagesize);
}
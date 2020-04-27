package com.example.myapplication.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AwardsListApi {

    @GET("BiSheServer/main?action=selectScholarYearsList")
    public Call<ResponseBody> getAwardsList();
}

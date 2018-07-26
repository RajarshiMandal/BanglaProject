package com.example.raju.demoBlog.data.network;

import com.example.raju.demoBlog.Utils.ApiConstantUtils;
import com.example.raju.demoBlog.data.database.model.BloggerApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BloggerService {

    @GET(ApiConstantUtils.SERVICE_CALL_URL)
    Call<BloggerApi> getFirstApiItems();

    @GET(ApiConstantUtils.SERVICE_CALL_URL)
    Call<BloggerApi> getNextApiItems(@Query("pageToken") String nextPage);

}
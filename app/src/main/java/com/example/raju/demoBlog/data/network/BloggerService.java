package com.example.raju.demoBlog.data.network;

import com.example.raju.demoBlog.data.database.model.BaseModel;
import com.example.raju.demoBlog.data.database.model.SingleItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.raju.demoBlog.Utils.UrlUtils.PATH;
import static com.example.raju.demoBlog.Utils.UrlUtils.QUERY_API_KEY;
import static com.example.raju.demoBlog.Utils.UrlUtils.QUERY_FETCH_BODIES;

public interface BloggerService {

    /**
     *
     */
    @GET(PATH + QUERY_API_KEY + QUERY_FETCH_BODIES)
    Call<BaseModel> getFirstCallItems();

    @GET(PATH + QUERY_API_KEY + QUERY_FETCH_BODIES)
    Call<BaseModel> getNextCallItems(@Query("pageToken") String nextPage);

    @GET(PATH + "/{id}" + QUERY_API_KEY)
    Call<SingleItem> getSingleCallItem(@Path("id") String contentId);

}
package com.example.raju.demoBlog.data.network;

import com.example.raju.demoBlog.data.database.model.BloggerApi;

import retrofit2.Call;
import retrofit2.Retrofit;

public class ApiClient {

    private static final String TAG = ApiClient.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static ApiClient sInstance;

    private BloggerService mBloggerService;

    private ApiClient(Retrofit retrofit) {
        mBloggerService = retrofit.create(BloggerService.class);
    }

    public static ApiClient getInstance(Retrofit retrofit) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ApiClient(retrofit);
                }
            }
        }
        return sInstance;
    }

    public Call<BloggerApi> fetchFirstNetworkCall() {
        return mBloggerService.getFirstApiItems();
    }

    public Call<BloggerApi> fetchNextNetworkCall(String nextPageToken) {
        return mBloggerService.getNextApiItems(nextPageToken);
    }

}

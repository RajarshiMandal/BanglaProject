package com.example.raju.demoBlog.data.network;

import com.example.raju.demoBlog.Utils.UrlUtils;
import com.example.raju.demoBlog.data.database.model.BaseModel;
import com.example.raju.demoBlog.data.database.model.SingleItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ApiClient {

    API_CLIENT(RetrofitFactory.RETROFIT_FACTORY.getRetrofit());

    private BloggerService mBloggerService;

    ApiClient(Retrofit retrofit) {
        mBloggerService = retrofit.create(BloggerService.class);
    }

    public Call<BaseModel> getFirstNetworkCall() {
        return mBloggerService.getFirstCallItems();
    }

    public Call<BaseModel> getNextNetworkCall(String nextPageToken) {
        return mBloggerService.getNextCallItems(nextPageToken);
    }

    public Call<SingleItem> getSingleItemCall(String contentId) {
        return mBloggerService.getSingleCallItem(contentId);
    }

    /*
     * Factory for creating and providing Retrofit single instance
     */
    private enum RetrofitFactory {
        RETROFIT_FACTORY;

        Retrofit getRetrofit() {
            return new Retrofit.Builder()
                    .baseUrl(UrlUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

//        public Gson getGson() {
//            return new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
//                    .create();
//        }
    }

}

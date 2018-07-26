package com.example.raju.demoBlog.data.network;

import android.util.Log;

import com.example.raju.demoBlog.Utils.ApiConstantUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static final String TAG = RetrofitFactory.class.getSimpleName();
    private static Retrofit sInstance;

    private RetrofitFactory() {}

    public static Retrofit getInstance() {
        if (sInstance == null) {
            sInstance = new Retrofit.Builder()
                    .baseUrl(ApiConstantUtils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return sInstance;
    }

    /* Not needed as of now */
    public static Gson getGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .create();
    }
}

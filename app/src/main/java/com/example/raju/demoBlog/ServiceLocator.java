package com.example.raju.demoBlog;

import android.content.Context;

import com.example.raju.demoBlog.data.ApiRepository;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.data.network.RetrofitFactory;
import com.example.raju.demoBlog.ui.ItemViewModelFactory;

import retrofit2.Retrofit;

public class ServiceLocator {

    private static ApiClient provideApiClient() {
        Retrofit retrofit = RetrofitFactory.getInstance();
        return ApiClient.getInstance(retrofit);
    }

    private static AppExecutors provideExecutors() {
        return AppExecutors.getInstance();
    }

    private static ApiRepository provideApiRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        return ApiRepository.getInstance(
                provideApiClient(),
                database,
                provideExecutors());
    }

    public static ItemViewModelFactory provideViewModelFactory(Context context) {
        return new ItemViewModelFactory(
                provideApiRepository(context),
                provideExecutors());
    }


}

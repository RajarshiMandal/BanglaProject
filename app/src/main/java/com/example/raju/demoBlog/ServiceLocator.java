package com.example.raju.demoBlog;

import android.content.Context;

import com.example.raju.demoBlog.data.ApiRepository;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.ui.ItemViewModelFactory;

public enum ServiceLocator {
    SERVICE_LOCATOR;

    public ApiClient provideApiClient() {
        return ApiClient.API_CLIENT;
    }

    public AppExecutors provideExecutors() {
        return AppExecutors.APP_EXECUTORS;
    }

    public AppDatabase provideDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }

    private ApiRepository provideApiRepository(Context context) {
        return ApiRepository.getInstance(
                provideApiClient(),
                provideDatabase(context),
                provideExecutors());
    }

    public ItemViewModelFactory provideViewModelFactory(Context context) {
        return new ItemViewModelFactory(provideApiRepository(context));
    }
}

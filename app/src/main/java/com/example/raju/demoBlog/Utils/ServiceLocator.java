package com.example.raju.demoBlog.Utils;

import android.content.Context;

import com.example.raju.demoBlog.data.ItemRepository;
import com.example.raju.demoBlog.data.SingleItemRepo;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.ui.ItemViewModelFactory;
import com.example.raju.demoBlog.ui.SingleItemViewModelFactory;

public enum ServiceLocator {
    INSTANCE;

    public ApiClient provideApiClient() {
        return ApiClient.API_CLIENT;
    }

    public AppExecutors provideExecutors() {
        return AppExecutors.APP_EXECUTORS;
    }

    public AppDatabase provideDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }

    public ItemRepository provideItemRepository(Context context) {
        return ItemRepository.getInstance(
                provideApiClient(),
                provideDatabase(context),
                provideExecutors());
    }

    public ItemViewModelFactory provideViewModelFactory(Context context) {
        return new ItemViewModelFactory(provideItemRepository(context));
    }

    public SingleItemRepo provideSingleItemRepo(Context context) {
        return SingleItemRepo.getInstance(
                provideApiClient(),
                provideDatabase(context),
                provideExecutors()
        );
    }

    public SingleItemViewModelFactory provideSingleItemViewModelFactory(Context context) {
        return new SingleItemViewModelFactory(provideSingleItemRepo(context));
    }
}

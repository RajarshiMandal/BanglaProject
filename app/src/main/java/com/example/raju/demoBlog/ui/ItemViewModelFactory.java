package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.ApiRepository;

public class ItemViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ApiRepository mApiRepository;
    private final AppExecutors mExecutors;

    public ItemViewModelFactory(ApiRepository apiRepository, AppExecutors executors) {
        mApiRepository = apiRepository;
        mExecutors = executors;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new ItemViewModel(mApiRepository, mExecutors));
    }
}

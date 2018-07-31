package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.raju.demoBlog.data.SingleItemRepo;

public class SingleItemViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SingleItemRepo mSingleItemRepo;

    public SingleItemViewModelFactory(SingleItemRepo singleItemRepo) {
        mSingleItemRepo = singleItemRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new SingleItemViewModel(mSingleItemRepo));
    }
}

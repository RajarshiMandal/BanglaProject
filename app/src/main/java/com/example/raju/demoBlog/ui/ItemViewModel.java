package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.ApiRepository;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.database.model.Item;

import java.util.List;

public class ItemViewModel extends ViewModel {

    private AppExecutors mExecutors;
    private ApiRepository mApiRepository;
    private LiveData<PagedList<Item>> mItemListObservable;
    private LiveData<NetworkState> mNetworkStateObservable;

    public ItemViewModel(ApiRepository apiRepository, AppExecutors executors) {
        mApiRepository = apiRepository;
        mExecutors = executors;
        mItemListObservable = mApiRepository.getItemListLiveData();
        mNetworkStateObservable = mApiRepository.getNetworkStateLiveData();
    }

    public LiveData<PagedList<Item>> getItemList() {
        return mItemListObservable;
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkStateObservable;
    }
}

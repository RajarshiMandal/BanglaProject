package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.raju.demoBlog.data.ApiRepository;
import com.example.raju.demoBlog.data.ItemBoundaryCallBack;
import com.example.raju.demoBlog.data.database.model.Item;

public class ItemViewModel extends ViewModel {

    private final ApiRepository mApiRepository;
    private final LiveData<PagedList<Item>> mItemListObservable;
    private final DataSource.Factory<Integer, Item> sourceFactory;

    private final ItemBoundaryCallBack boundaryCallBack;

    public ItemViewModel(ApiRepository apiRepository) {
        mApiRepository = apiRepository;

        boundaryCallBack = new ItemBoundaryCallBack(mApiRepository);
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(true)
                .build();
        sourceFactory = mApiRepository.getDataSourceFactory();
        mItemListObservable = new LivePagedListBuilder<>(sourceFactory, pageConfig)
                .setBoundaryCallback(boundaryCallBack)
                .build();
    }

    public LiveData<PagedList<Item>> getItemList() {
        return mItemListObservable;
    }

}

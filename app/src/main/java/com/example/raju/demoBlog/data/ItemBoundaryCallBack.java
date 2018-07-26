package com.example.raju.demoBlog.data;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.raju.demoBlog.data.database.model.Item;

public class ItemBoundaryCallBack extends PagedList.BoundaryCallback<Item> {

    private static final String TAG = ItemBoundaryCallBack.class.getSimpleName();
    private final ApiRepository mRepository;

    public ItemBoundaryCallBack(ApiRepository repository) {
        mRepository = repository;
    }

    @Override
    public void onZeroItemsLoaded() {
        mRepository.getFirstCallback();
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Item itemAtEnd) {
        mRepository.getNextCallbacks();
    }
}

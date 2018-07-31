package com.example.raju.demoBlog.data;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.example.raju.demoBlog.data.database.model.Item;

public class ItemBoundaryCallBack extends PagedList.BoundaryCallback<Item> {

    private final ItemRepository mRepository;

    public ItemBoundaryCallBack(ItemRepository repository) {
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

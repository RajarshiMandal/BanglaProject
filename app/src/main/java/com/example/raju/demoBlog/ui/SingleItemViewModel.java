package com.example.raju.demoBlog.ui;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.raju.demoBlog.data.SingleItemRepo;
import com.example.raju.demoBlog.data.database.model.SingleItem;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.network.RetryCallback;

public class SingleItemViewModel extends ViewModel {

    private SingleItemRepo mSingleItemRepo;
    private MutableLiveData<Long> mSingleItemMld;
    private LiveData<SingleItem> mSingleItemLiveData;

    SingleItemViewModel(SingleItemRepo singleItemRepo) {
        mSingleItemRepo = singleItemRepo;
        // Initialize and set the value of Item ID for the search term
        mSingleItemMld = new MutableLiveData<>();
        mSingleItemMld.setValue(null);

        initSingleItemLiveData();
    }

    private void initSingleItemLiveData() {
        if (mSingleItemLiveData == null) {
            mSingleItemLiveData = Transformations.switchMap(mSingleItemMld, new Function<Long, LiveData<SingleItem>>() {
                @Override
                public LiveData<SingleItem> apply(Long itemId) {
                    return mSingleItemRepo.getSingleItemLiveData(itemId);
                }
            });
        }
    }

    public void setSearchApiId(long itemId, String callId) {
        mSingleItemMld.setValue(itemId);
        mSingleItemRepo.callSingleItemAndSaveData(itemId, callId);
    }

    public LiveData<SingleItem> getSingleItemLiveData() {
        return mSingleItemLiveData;
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return mSingleItemRepo.getNetworkStateLiveData();
    }

    public void setSingleItemRetry(RetryCallback<SingleItem> singleItemRetry) {
        mSingleItemRepo.setSingleItemRetry(singleItemRetry);
    }
}

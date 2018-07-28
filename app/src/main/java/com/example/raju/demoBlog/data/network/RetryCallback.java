package com.example.raju.demoBlog.data.network;

import android.arch.lifecycle.MutableLiveData;

import retrofit2.Call;

public interface RetryCallback<T> {
    void getCall(Call<T> call,
                 ApiCallback<T> callback,
                 MutableLiveData<NetworkState> networkStateObserver);
}

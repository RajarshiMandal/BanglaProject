package com.example.raju.demoBlog.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.database.model.BloggerApi;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.data.network.NetworkState;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemBoundaryCallBack extends PagedList.BoundaryCallback<Item> {

    private static final String TAG = ItemBoundaryCallBack.class.getSimpleName();
    private final ApiClient mClient;
    private final AppExecutors mExecutors;
    private final ApiRepository mRepository;
    private MutableLiveData<NetworkState> mObservableNetwork;
    private String mNextPageToken;

    ItemBoundaryCallBack(ApiClient client, AppExecutors executors, ApiRepository repository) {
        mClient = client;
        mExecutors = executors;
        mRepository = repository;
        mObservableNetwork = new MutableLiveData<>();
    }

    @Override
    public void onZeroItemsLoaded() {
        Log.d(TAG, "onZeroItemsLoaded: Called");
        if (isFetchNeeded()) // Todo can be useful for checking new content if removed
            mClient.fetchFirstNetworkCall().enqueue(getRetrofitCallback());
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Item itemAtEnd) {
        if (mNextPageToken != null)
            mClient.fetchNextNetworkCall(mNextPageToken).enqueue(getRetrofitCallback());
    }

    @NonNull
    private Callback<BloggerApi> getRetrofitCallback() {
        return new Callback<BloggerApi>() {
            @Override
            public void onResponse(@NonNull Call<BloggerApi> call, @NonNull Response<BloggerApi> response) {
                mObservableNetwork.setValue(NetworkState.LOADING);
                if (response.isSuccessful()) {
                    final BloggerApi responseBody = response.body();
                    if (responseBody != null) {
                        mNextPageToken = responseBody.getNextPageToken();

                        mExecutors.diskIO().execute(() -> {
                            List<Item> itemList = responseBody.getItems();
                            if (!(itemList == null || itemList.isEmpty())) {
                                insertItemsToDb(responseBody.getItems());
                            }
                            mObservableNetwork.postValue(NetworkState.SUCCESS);
                        });
                    }
                } else {
                    String error = response.errorBody() == null ? "Unknown Error" : String.valueOf(response.errorBody());
                    mObservableNetwork.setValue(NetworkState.error(error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloggerApi> call, @NonNull Throwable t) {
                mObservableNetwork.setValue(NetworkState.error(t.getMessage()));
            }
        };
    }

    private void insertItemsToDb(List<Item> items) {
        mRepository.insertAllItems(items);
    }

    private boolean isFetchNeeded() {
        int count = 0;
        try {
            count = mExecutors.diskIO().submit(mRepository::fetchItemCount).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (count <= 0);
    }

    public LiveData<NetworkState> getObservableNetwork() {
        return mObservableNetwork;
    }
}

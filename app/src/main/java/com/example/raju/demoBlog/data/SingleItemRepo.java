package com.example.raju.demoBlog.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.raju.demoBlog.Utils.AppExecutors;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.model.SingleItem;
import com.example.raju.demoBlog.data.network.ApiCallback;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.network.RetryCallback;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class SingleItemRepo {

    private static final Object LOCK = new Object();
    private static final String TAG = SingleItemRepo.class.getSimpleName();
    private static SingleItemRepo sInstance;

    private ApiClient mApiClient;
    private AppDatabase mDatabase;
    private AppExecutors mExecutors;
    private final MutableLiveData<NetworkState> mNetworkStateMld;
    private RetryCallback<SingleItem> mSingleItemRetry;

    private SingleItemRepo(ApiClient apiClient, AppDatabase database, AppExecutors executors) {
        mApiClient = apiClient;
        mDatabase = database;
        mExecutors = executors;
        mNetworkStateMld = new MutableLiveData<>();
        mNetworkStateMld.setValue(NetworkState.SUCCESS);
    }

    public static SingleItemRepo getInstance(ApiClient apiClient, AppDatabase database, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null)
                    sInstance = new SingleItemRepo(apiClient, database, executors);
            }
        }
        return sInstance;
    }

    private boolean isCallSingleItemNeeded(final long apiId) {
        long dbApiId = 0L;
        try {
            dbApiId = mExecutors.diskIO().submit(() -> fetchSingleItemId(apiId)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return apiId != dbApiId;
    }

    private ApiCallback<SingleItem> getSingleItemCallback(final long apiId) {
        mNetworkStateMld.setValue(NetworkState.LOADING);
        return new ApiCallback<SingleItem>() {

            @Override
            public void onSuccess(@NonNull final SingleItem responseBody) {
                Log.d(TAG, "Retrofit got response");
                mExecutors.diskIO().execute(() -> {
                    mNetworkStateMld.postValue(NetworkState.SAVING);
                    insertSingleItem(apiId, responseBody.getContent());
                    mNetworkStateMld.postValue(NetworkState.SUCCESS);
                });
            }

            @Override
            public void onError(@NonNull Call<SingleItem> call, @NonNull String message) {
                mNetworkStateMld.setValue(NetworkState.Error(message));
                mSingleItemRetry.getCall(call, this, mNetworkStateMld);
            }
        };
    }

    /*
     * Helper methods
     */

    private void insertSingleItem(long apiId, String content) {
        mDatabase.singleItemDao().insertSingleItem(new SingleItem(apiId, content));
    }

    private long fetchSingleItemId(long apiId) {
        return mDatabase.singleItemDao().fetchSingleItemId(apiId);
    }

    /*
     * Public methods
     */

    public void callSingleItemAndSaveData(long apiId, String callId) {
        if (isCallSingleItemNeeded(apiId))
            mApiClient.getSingleItemCall(callId).enqueue(getSingleItemCallback(apiId));
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return mNetworkStateMld;
    }

    public LiveData<SingleItem> getSingleItemLiveData(long apiId) {
        return mDatabase.singleItemDao().fetchSingleItem(apiId);
    }

    public void setSingleItemRetry(RetryCallback<SingleItem> mSingleItemRetry) {
        this.mSingleItemRetry = mSingleItemRetry;
    }
}

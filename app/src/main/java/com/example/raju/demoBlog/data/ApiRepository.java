package com.example.raju.demoBlog.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.data.network.NetworkState;

public class ApiRepository extends DatabaseHelper {

    private static final String TAG = ApiRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static ApiRepository sInstance;
    private final ApiClient mClient;
    private final AppExecutors mExecutors;
    private LiveData<NetworkState> mNetworkState;

    private ApiRepository(ApiClient client, AppDatabase database, AppExecutors executors) {
        super(database);
        mClient = client;
        mExecutors = executors;
    }

    public static ApiRepository getInstance(ApiClient client, AppDatabase database, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ApiRepository(client, database, executors);
                }
            }
        }

        return sInstance;
    }

    public LiveData<PagedList<Item>> getItemListLiveData() {
        ItemBoundaryCallBack boundaryCallBack = new ItemBoundaryCallBack(mClient, mExecutors, this);
        mNetworkState = boundaryCallBack.getObservableNetwork();
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(true)
                .build();
        return new LivePagedListBuilder<>(fetchAllItems(), pageConfig)
                .setBoundaryCallback(boundaryCallBack)
                .build();
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return mNetworkState;
    }
}

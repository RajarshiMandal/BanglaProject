package com.example.raju.demoBlog.data;

import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.dao.NextPageTokenDao;
import com.example.raju.demoBlog.data.database.model.BloggerApi;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.database.model.NextPageToken;
import com.example.raju.demoBlog.data.network.ApiClient;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {

    private static final String TAG = ApiRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static ApiRepository sInstance;

    private final ApiClient mClient;
    private final AppExecutors mExecutors;

    private final ItemDao itemDao;
    private final NextPageTokenDao nextPageTokenDao;

    private ApiRepository(ApiClient client, AppDatabase database, AppExecutors executors) {
        mClient = client;
        mExecutors = executors;
        itemDao = database.itemDao();
        nextPageTokenDao = database.nextPageTokenDao();
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

    public void getFirstCallback() {
        if (isFetchNeeded()) // Todo can be useful for checking new content if removed
            mClient.fetchFirstNetworkCall().enqueue(getRetrofitCallback());
    }

    private boolean isFetchNeeded() {
        int count = 0;
        try {
            count = mExecutors.diskIO().submit(itemDao::fetchCount).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return (count <= 0);
    }

    public void getNextCallbacks() {
        String nextPageToken = getDbPageToken();
        if (nextPageToken != null)
            mClient.fetchNextNetworkCall(nextPageToken).enqueue(getRetrofitCallback());
    }

    private String getDbPageToken() {
        String nextPageToken = null;
        try {
            nextPageToken = mExecutors.diskIO().submit(nextPageTokenDao::fetchDbPageToken).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return nextPageToken;
    }

    private Callback<BloggerApi> getRetrofitCallback() {
        return new Callback<BloggerApi>() {
            @Override
            public void onResponse(@NonNull Call<BloggerApi> call, @NonNull Response<BloggerApi> response) {
                if (response.isSuccessful()) {
                    BloggerApi responseBody = response.body();
                    if (responseBody != null) {
                        mExecutors.diskIO().execute(() -> {
                            insertNextPageToken(responseBody.getNextPageToken());
                            insertItems(responseBody);
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BloggerApi> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        };
    }

    private void insertItems(BloggerApi responseBody) {
        itemDao.insertItems(responseBody.getItems());
    }

    private void insertNextPageToken(String nextPageToken) {
        nextPageTokenDao.insertNextPageToken(new NextPageToken(nextPageToken));
    }

    public DataSource.Factory<Integer, Item> getDataSourceFactory() {
        return itemDao.fetchAllItems();
    }
}

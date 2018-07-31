package com.example.raju.demoBlog.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.util.Log;

import com.example.raju.demoBlog.Utils.AppExecutors;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.dao.ItemTagsDao;
import com.example.raju.demoBlog.data.database.dao.NextPageTokenDao;
import com.example.raju.demoBlog.data.database.dao.TagDao;
import com.example.raju.demoBlog.data.database.model.BaseModel;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.database.model.ItemTagRelation;
import com.example.raju.demoBlog.data.database.model.NextPageToken;
import com.example.raju.demoBlog.data.database.model.Tag;
import com.example.raju.demoBlog.data.network.ApiCallback;
import com.example.raju.demoBlog.data.network.ApiClient;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.network.RetryCallback;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

public class ItemRepository {

    private static final String TAG = ItemRepository.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static ItemRepository sInstance;

    private final ApiClient mClient;
    private final AppExecutors mExecutors;

    private final ItemDao itemDao;
    private final NextPageTokenDao nextPageTokenDao;
    private final ItemTagsDao itemTagsDao;
    private final TagDao tagDao;

    private final MutableLiveData<NetworkState> networkStateObservable;
    private final AppDatabase database;

    private RetryCallback<BaseModel> retryCallback;

    private ItemRepository(ApiClient client, AppDatabase database, AppExecutors executors) {
        this.database = database;
        mClient = client;
        mExecutors = executors;
        nextPageTokenDao = database.nextPageTokenDao();
        itemDao = database.itemDao();
        tagDao = database.tagDao();
        itemTagsDao = database.itemTagsDao();

        networkStateObservable = new MutableLiveData<>();
    }

    public static ItemRepository getInstance(ApiClient client, AppDatabase database, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ItemRepository(client, database, executors);
                }
            }
        }
        return sInstance;
    }

    public void getFirstCallback() {
        if (isFetchNeeded()) {
            mClient.getFirstNetworkCall().enqueue(getRetrofitCallback());
        }
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
        if (nextPageToken != null) {
            networkStateObservable.setValue(NetworkState.LOADING);
            mClient.getNextNetworkCall(nextPageToken).enqueue(getRetrofitCallback());
        }
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

    private ApiCallback<BaseModel> getRetrofitCallback() {
        networkStateObservable.setValue(NetworkState.LOADING);
        return new ApiCallback<BaseModel>() {

            @Override
            protected void onSuccess(final BaseModel responseBody) {
                if (responseBody != null) {
                    mExecutors.diskIO().execute(() -> {
                        networkStateObservable.postValue(NetworkState.SAVING);
                        insertAllToDb(responseBody);
                        networkStateObservable.postValue(NetworkState.SUCCESS);
                    });
                }
            }

            @Override
            protected void onError(Call<BaseModel> call, String s) {
                networkStateObservable.setValue(NetworkState.Error(s));
                retryCallback.getCall(call, this, networkStateObservable);
            }
        };
    }

    private void insertAllToDb(BaseModel responseBody) {
        List<Item> items = responseBody.getItems();
        // Check for empty body
        if (items == null || items.isEmpty()) return;

        insertNextPageToken(responseBody.getNextPageToken());
        List<Long> itemIds = insertItemList(items);
        for (int i = 0; i < items.size(); i++) {
            long itemId = itemIds.get(i);
            // Check if no duplicate Items are inserted
            if (itemId != -1) {
                List<String> labelNames = items.get(i).getTags();
                for (String labelName : labelNames) {
                    // Insert Tag Names
                    insertTags(labelName);
                    // Get the id of the tag name
                    int tagId = fetchTagId(labelName);
                    // Insert the relational mDatabase
                    insetItemTagRelation(itemId, tagId);
                }
            } else {
                Log.d(TAG, "insertItemsToDb: failed " + items.get(i).getTitle());
            }
        }
    }

    /*
     * Database helper methods
     */

    private void insertNextPageToken(String nextPageToken) {
        nextPageTokenDao.insertNextPageToken(new NextPageToken(nextPageToken));
    }

    private List<Long> insertItemList(List<Item> itemList) {
        return itemDao.insertItems(itemList);
    }

    private void insertTags(String tagName) {
        tagDao.insetTagName(new Tag(tagName));
    }

    private int fetchTagId(String tagName) {
        return tagDao.fetchIdByTagName(tagName);
    }

    private void insetItemTagRelation(long item_id, int tag_id) {
        itemTagsDao.insetItemTagRelation(new ItemTagRelation(item_id, tag_id));
    }

    /*
     * Publicly available getters and setters
     */

    public DataSource.Factory<Integer, Item> getDataSourceFactory() {
        return itemDao.fetchAllItems();
    }

    public List<String> fetchTags(long itemId) {
        return itemTagsDao.fetchTagNameById(itemId);
    }

    public AppExecutors getExecutors() {
        return mExecutors;
    }

    public LiveData<NetworkState> getNetworkStateObservable() {
        return networkStateObservable;
    }

    public void setRetryCallback(RetryCallback<BaseModel> retryCallback) {
        this.retryCallback = retryCallback;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}

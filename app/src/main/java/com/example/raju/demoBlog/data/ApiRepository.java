package com.example.raju.demoBlog.data;

import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.raju.demoBlog.AppExecutors;
import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.dao.ItemTagsDao;
import com.example.raju.demoBlog.data.database.dao.NextPageTokenDao;
import com.example.raju.demoBlog.data.database.dao.TagDao;
import com.example.raju.demoBlog.data.database.model.BloggerApi;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.database.model.ItemTagRelation;
import com.example.raju.demoBlog.data.database.model.NextPageToken;
import com.example.raju.demoBlog.data.database.model.Tag;
import com.example.raju.demoBlog.data.network.ApiClient;

import java.util.List;
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
    private final ItemTagsDao itemTagsDao;
    private final TagDao tagDao;

    private ApiRepository(ApiClient client, AppDatabase database, AppExecutors executors) {
        mClient = client;
        mExecutors = executors;
        nextPageTokenDao = database.nextPageTokenDao();
        itemDao = database.itemDao();
        tagDao = database.tagDao();
        itemTagsDao = database.itemTagsDao();

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
                            insertAllToDb(responseBody);
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

    private void insertAllToDb(BloggerApi responseBody) {
        List<Item> items = responseBody.getItems();
        if (items != null && !items.isEmpty()) {
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
                        // Insert the relational database
                        insetItemTagRelation(itemId, tagId);
                    }
                } else {
                    Log.d(TAG, "insertItemsToDb: failed " + items.get(i).getTitle());
                }
            }
        }
    }

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

    public DataSource.Factory<Integer, Item> getDataSourceFactory() {
        return itemDao.fetchAllItems();
    }

    public List<String> fetchTags(long itemId) {
        return itemTagsDao.fetchTagNameById(itemId);
    }

    public AppExecutors getExecutors() {
        return mExecutors;
    }
}

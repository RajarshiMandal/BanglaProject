package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.raju.demoBlog.data.ItemBoundaryCallBack;
import com.example.raju.demoBlog.data.ItemRepository;
import com.example.raju.demoBlog.data.database.model.BaseModel;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.network.RetryCallback;

public class ItemViewModel extends ViewModel {

    private final static String TAG = ItemViewModel.class.getSimpleName();

    private final ItemRepository mApiRepository;
    private LiveData<PagedList<Item>> mItemListLiveData;
    private final DataSource.Factory<Integer, Item> mSourceFactory;

    public ItemViewModel(ItemRepository apiRepository) {
        mApiRepository = apiRepository;

        ItemBoundaryCallBack boundaryCallBack = new ItemBoundaryCallBack(mApiRepository);
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setPageSize(10)
                .setEnablePlaceholders(true)
                .build();
        mSourceFactory = mApiRepository.getDataSourceFactory();
        if (mItemListLiveData == null) {
            mItemListLiveData = new LivePagedListBuilder<>(mSourceFactory, pageConfig)
                    .setBoundaryCallback(boundaryCallBack)
                    .build();
        }

        mItemListLiveData = Transformations.switchMap(mItemListLiveData, pagedList -> {
            MutableLiveData<PagedList<Item>> mutableLiveData = new MutableLiveData<>();
            mApiRepository.getExecutors().diskIO().execute(() -> {
                for (Item item : pagedList) {
                    item.setTags(mApiRepository.fetchTags(item.getItem_id()));
                    mutableLiveData.postValue(pagedList);
                }
            });
            return mutableLiveData;
        });

//        // For TAG RETURNING AS LIVEDATA
//        mItemListLiveData = Transformations.switchMap(mItemListLiveData, new Function<PagedList<Item>, LiveData<PagedList<Item>>>() {
//            @Override
//            public LiveData<PagedList<Item>> apply(PagedList<Item> input) {
//                MediatorLiveData<PagedList<Item>> tagMediator = new MediatorLiveData<>();
//                for (Item item : input) {
//                    tagMediator.addSource(mApiRepository.fetchTags(item.getItem_id()), new Observer<List<String>>() {
//                        @Override
//                        public void onChanged(@Nullable List<String> tags) {
//                            item.setTags(tags);
//                            tagMediator.postValue(input);
//                        }
//                    });
//                }
//                Log.d(TAG, "apply: " + tagMediator.getValue());
//                return tagMediator;
//            }
//        });
    }

    public LiveData<PagedList<Item>> getItemListLiveData() {
        return mItemListLiveData;
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return mApiRepository.getNetworkStateObservable();
    }

    public void setRetryCallback(RetryCallback<BaseModel> retryCallback) {
        mApiRepository.setRetryCallback(retryCallback);
    }

    public DataSource.Factory<Integer, Item> getSourceFactory() {
        return mSourceFactory;
    }

    public ItemRepository getApiRepository() {
        return mApiRepository;
    }
}

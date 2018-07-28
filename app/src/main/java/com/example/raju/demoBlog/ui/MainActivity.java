package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.ServiceLocator;
import com.example.raju.demoBlog.data.network.NetworkState;
import com.example.raju.demoBlog.data.network.Status;
import com.example.raju.demoBlog.ui.recyclerview.ItemAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ItemViewModel viewModel;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button retryButton = findViewById(R.id.retry);

        initViewModel(this);
        initRecyclerView();
        observeViewModel(adapter);

        retryButton.setOnClickListener(view -> {
            ServiceLocator.SERVICE_LOCATOR.provideExecutors().diskIO().execute(() -> {
                viewModel.getApiRepository().getDatabase().clearAllTables();
                viewModel.getSourceFactory().create().invalidate();
            });
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel(Context context) {
        ItemViewModelFactory factory = ServiceLocator.SERVICE_LOCATOR.provideViewModelFactory(context);
        viewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);
    }

    private void observeViewModel(ItemAdapter adapter) {
        // Observe list of Items
        viewModel.getItemListLiveData().observe(this, adapter::submitList);
        // Observe network states
        viewModel.getNetworkStateLiveData().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
            // Setting up Retry Callback
            if (networkState == null) return;

            if (networkState.getStatus() == Status.ERROR) {
                viewModel.setRetryCallback((call, callback, networkStateObserver) ->
                        adapter.setListener(() -> {
                            // Required otherwise getting null object on second retry
                            networkStateObserver.postValue(NetworkState.RETRY);
                            call.clone().enqueue(callback);
                        })
                );
            }
        });
    }
}

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ItemViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button retryButton = findViewById(R.id.retry);

        initViewModel(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        mViewModel.getItemListLiveData().observe(this, adapter::submitList);

    }

    private void initViewModel(Context context) {
        ItemViewModelFactory factory = ServiceLocator.provideViewModelFactory(context);
        mViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);
    }
}

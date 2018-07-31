package com.example.raju.demoBlog.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.Utils.IntentConstants;
import com.example.raju.demoBlog.Utils.ServiceLocator;
import com.example.raju.demoBlog.data.network.NetworkState;

public class SingleItemActivity extends AppCompatActivity {

    private static final String TAG = SingleItemActivity.class.getSimpleName();

    private TextView mContentView;
    private ProgressBar mProgressBar;
    private TextView mInfoView;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        Log.d(TAG, "_____ SingleItem");
        Toolbar toolbar = findViewById(R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mContentView = findViewById(R.id.content_view);
        mProgressBar = findViewById(R.id.progressbar);
        mInfoView = findViewById(R.id.network_state_info);
        mFab = findViewById(R.id.fab);

        Intent intent = getIntent();
        String callId = intent.getStringExtra(IntentConstants.EXTRA_CALL_ITEM_ID);
        long itemId = intent.getLongExtra(IntentConstants.EXTRA_ITEM_LONG_ID, 0L);

        SingleItemViewModel viewModel = getSingleItemViewModel();
        // Set the ids to initiate a call or load from Db
        viewModel.setSearchApiId(itemId, callId);
        // Observer network states
        observerNetworkState(viewModel);
        // Observer Single Item content
        observeSingleItem(viewModel);
    }

    @NonNull
    private SingleItemViewModel getSingleItemViewModel() {
        SingleItemViewModelFactory factory = ServiceLocator.INSTANCE.provideSingleItemViewModelFactory(this);
        return ViewModelProviders.of(this, factory).get(SingleItemViewModel.class);
    }

    private void observerNetworkState(final SingleItemViewModel viewModel) {
        viewModel.getNetworkStateLiveData().observe(this, networkState -> {
            if (networkState == null) return;
            String message = networkState.getMessage();
            switch (networkState.getStatus()) {
                case SUCCESS:
                    loadingIndicator(message, View.GONE);
                    break;
                case ERROR:
                    mProgressBar.setVisibility(View.GONE);
                    mInfoView.setText(message);
                    viewModel.setSingleItemRetry((call, callback, networkStateObserver) -> {
                        Snackbar.make(mInfoView, "Connection Problem. Retry?", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", view -> {
                                    networkStateObserver.postValue(NetworkState.RETRY);
                                    call.clone().enqueue(callback);
                                }).show();
                    });
                    break;
                default:
                    loadingIndicator(message, View.VISIBLE);
                    break;
            }
        });
    }

    private void loadingIndicator(String message, int visibility) {
        mProgressBar.setVisibility(visibility);
        mInfoView.setVisibility(visibility);
        mInfoView.setText(message);
    }

    private void observeSingleItem(SingleItemViewModel viewModel) {
        viewModel.getSingleItemLiveData().observe(this, singleItem -> {
            if (singleItem == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContentView.setText(Html.fromHtml(singleItem.getContent(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mContentView.setText(Html.fromHtml(singleItem.getContent()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package com.example.raju.demoBlog.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.data.network.NetworkState;


class NetworkStateViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = NetworkStateViewHolder.class.getSimpleName();

    private final ProgressBar progressBar;
    private final View errorView;
    private final TextView infoMessageView;

    NetworkStateViewHolder(View itemView, RetryListener retryListener) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressbar);
        errorView = itemView.findViewById(R.id.error_view);
        infoMessageView = itemView.findViewById(R.id.info_view);
        Button retryButton = itemView.findViewById(R.id.retry_button);

        retryButton.setOnClickListener(view -> retryListener.retry());
    }

    void bind(NetworkState networkState) {
        String infoMessage = networkState.getMessage();
        Log.d(TAG, "bind: " + infoMessage);

        progressBar.setVisibility(progressbarVisibility(networkState));
        errorView.setVisibility(errorVisibility(networkState));
        infoMessageView.setText(infoMessage);
    }

    private int progressbarVisibility(NetworkState shouldShow) {
        switch (shouldShow.getStatus()) {
            case SUCCESS:
                return View.GONE;
            case ERROR:
                return View.GONE;
            default:
                return View.VISIBLE;
        }
    }

    private int errorVisibility(NetworkState shouldShow) {
        switch (shouldShow.getStatus()) {
            case ERROR:
                return View.VISIBLE;
            default:
                return View.GONE;
        }
    }
}
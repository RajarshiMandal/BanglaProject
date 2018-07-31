package com.example.raju.demoBlog.ui.recyclerview;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.network.NetworkState;

import java.util.List;

public class ItemAdapter extends PagedListAdapter<Item, RecyclerView.ViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();
    private final int mNetworkStateView = R.layout.networkstate_item;
    private final int mItemListView = R.layout.list_item;
    private NetworkState networkState;
    private RetryListener listener;
    private int mCount;

    public ItemAdapter() {
        super(Item.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case mItemListView:
                view = inflater.inflate(mItemListView, parent, false);
                return new ItemViewHolder(view);
            case mNetworkStateView:
                view = inflater.inflate(mNetworkStateView, parent, false);
                return new NetworkStateViewHolder(view, listener);
            default:
                throw new IllegalArgumentException("unknown view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case mItemListView:
                Item item = getItem(position);
                if (item == null) return;
                // Setting the tags
                String[] tagsToSet = getTags(item);
                // Setting the visibility
                int visibility = getChipVisibility(tagsToSet[1]);
                ((ItemViewHolder) holder).bind(item, tagsToSet[0], tagsToSet[1], visibility);
                break;
            case mNetworkStateView:
                ((NetworkStateViewHolder) holder).bind(networkState);
                break;
            default:
                throw new IllegalArgumentException("unknown holder for position " + position);
        }
    }

    private int getChipVisibility(String str) {
        int visibility = 0;
        if (TextUtils.isEmpty(str)) visibility = View.GONE;
        return visibility;
    }

    @NonNull
    private String[] getTags(Item item) {
        List<String> tagList = item.getTags();
        String[] tagsToSet = new String[2];
        tagsToSet[0] = "Oops";
        tagsToSet[1] = "";
        for (int i = 0; i < tagList.size(); i++) {
            tagsToSet[i] = tagList.get(i);
        }
        return tagsToSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && (position == (getItemCount() - 1))) {
            return mNetworkStateView;
        }
        return mItemListView;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return hasExtraRow() ? itemCount + 1 : itemCount;
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = networkState;
        // Check if the previous row had success
        boolean hadExtraRow = hasExtraRow();
        // Set the new state
        networkState = newNetworkState;
        // Check if the new state
        boolean hasExtraRow = hasExtraRow(); //true
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        return (networkState != null) && (networkState != NetworkState.SUCCESS);
    }

    public void setListener(RetryListener listener) {
        this.listener = listener;
    }
}

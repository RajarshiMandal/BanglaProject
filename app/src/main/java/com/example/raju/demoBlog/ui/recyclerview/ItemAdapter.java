package com.example.raju.demoBlog.ui.recyclerview;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.network.NetworkState;

public class ItemAdapter extends PagedListAdapter<Item, RecyclerView.ViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();
    private final int mNetworkStateView = R.layout.networkstate_item;
    private final int mItemListView = R.layout.list_item;
    private NetworkState networkState;
    private RetryListener listener;

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
                ((ItemViewHolder) holder).bind(item);
                break;
            case mNetworkStateView:
                ((NetworkStateViewHolder) holder).bind(networkState);
                break;
        }
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
        boolean hadExtraRow = hasExtraRow(); //false
        networkState = newNetworkState;//loading
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

    //    static class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView titleView;
//        private TextView[] tagViews = new TextView[2];
//        //        private final TextView label1;
//
//        ItemViewHolder(View itemView) {
//            super(itemView);
//            titleView = itemView.findViewById(R.id.item_title);
//            tagViews[0] = itemView.findViewById(R.id.tag0);
//            tagViews[1] = itemView.findViewById(R.id.tag1);
//
//        }
//
//        void bind(Item item) {
//            // Get the position while binding
////            position = getAdapterPosition();
//
////            apiId = item.getItem_id();
////            callId = item.getId();
//
//            titleView.setText(item.getTitle());
//            List<String> tags = item.getTags();
//            // Get the list size and set it to maximum 2
//            int finalSize = tags.size() > 2 ? 2 : 1;
//            for (int i = 0; i < finalSize; i++) {
//                tagViews[i].setText(tags.get(i));
//            }
//        }
//    }
//
//    class NetworkStateViewHolder extends RecyclerView.ViewHolder {
//    private final ProgressBar progressBar;
//    private final View errorView;
//    private final TextView infoMessageView;
//
//    NetworkStateViewHolder(View itemView) {
//        super(itemView);
//        progressBar = itemView.findViewById(R.id.progressbar);
//        errorView = itemView.findViewById(R.id.error_view);
//        infoMessageView = itemView.findViewById(R.id.info_view);
//        Button retryButton = itemView.findViewById(R.id.retry_button);
//
//    }
//
//    void bind(NetworkState networkState) {
//        String infoMessage = networkState.getMessage();
//        Log.d(TAG, "bind: " + infoMessage);
//        progressBar.setVisibility(progressbarVisibility(networkState));
//        errorView.setVisibility(errorVisibility(networkState));
//        infoMessageView.setText(infoMessage);
//    }
//
//    private int progressbarVisibility(NetworkState shouldShow) {
//        switch (shouldShow.getStatus()) {
//            case SUCCESS:
//                return View.GONE;
//            case ERROR:
//                return View.GONE;
//            default:
//                return View.VISIBLE;
//        }
//    }
//
//    private int errorVisibility(NetworkState shouldShow) {
//        switch (shouldShow.getStatus()) {
//            case ERROR:
//                return View.VISIBLE;
//            default:
//                return View.GONE;
//        }
//    }
//
////    private int visibility(boolean shouldShow) {
////        switch (shouldShow)
////        if (shouldShow)
////            return View.VISIBLE;
////        else
////            return View.GONE;
////    }
//}
}

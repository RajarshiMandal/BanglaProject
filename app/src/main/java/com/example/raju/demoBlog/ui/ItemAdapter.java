package com.example.raju.demoBlog.ui;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.data.database.model.Item;

import java.util.List;

public class ItemAdapter extends PagedListAdapter<Item, ItemAdapter.ItemViewHolder> {

    private static final String TAG = ItemAdapter.class.getSimpleName();

    public ItemAdapter() {
        super(Item.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = getItem(position);
        try {
            Log.d(TAG, "onBindViewHolder: " + item.getTags());
            Log.d(TAG, "onBindViewHolder: " + item.getItem_id());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        List<String> tags = item.getTags();

        String tag0 = "Tag 1", tag1 = "Tag 2";
        // TODO: Open the comments
        if (tags != null) {
            switch (tags.size()) {
                case 2:
                    tag0 = tags.get(0);
                    tag1 = tags.get(1);
                    break;
                default:
                    tag0 = tags.get(0);
                    tag1 = "";
                    break;
            }
        }
        holder.bind(item.getTitle(), tag0, tag1);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleView;
        private final TextView label0;
        private final TextView label1;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.item_title);
            label0 = itemView.findViewById(R.id.tag0);
            label1 = itemView.findViewById(R.id.tag1);
        }

        void bind(String title, String tag0, String tag1) {
            mTitleView.setText(title);
            label0.setText(tag0);
            label1.setText(tag1);
        }
    }

}

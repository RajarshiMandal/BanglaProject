package com.example.raju.demoBlog.ui.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.Utils.IntentConstants;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.ui.SingleItemActivity;

class ItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ItemViewHolder.class.getSimpleName();
    private final TextView titleView;
    //    private final TextView mTagView0;
//    private final TextView mTagView1;
    private final Chip tag0Chip;
    private final Chip tag1Chip;
    private int position;
    private long longId;
    private String callStringId;

    ItemViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.item_title);
//        mTagView0 = itemView.findViewById(R.id.tag0);
//        mTagView1 = itemView.findViewById(R.id.tag1);
        tag0Chip = itemView.findViewById(R.id.chip_tag0);
        tag1Chip = itemView.findViewById(R.id.chip_tag1);


        titleView.setOnClickListener(titleView -> {
            Context context = titleView.getContext();
            Intent intent = new Intent(context, SingleItemActivity.class);
            intent.putExtra(IntentConstants.EXTRA_ITEM_LONG_ID, longId);
            intent.putExtra(IntentConstants.EXTRA_CALL_ITEM_ID, callStringId);
            // Important to check if intent can resolve the activity
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        });
        // todo: implement tag clickListener
    }

    void bind(Item item, String tag0, String tag1, int visibility) {
        // Get the position while binding
        position = getAdapterPosition();

        longId = item.getItem_id();
        callStringId = item.getId();

        titleView.setText(item.getTitle());
        tag0Chip.setText(tag0);

        tag1Chip.setText(tag1);
        tag1Chip.setVisibility(visibility);
//        mTagView0.setText(tag0);
//        mTagView1.setText(tag1);
    }

//    private void titleClickListener(View view) {
//        if (canSetListener())
//            onItemClickListener.onTitleClick(view, position);
//    }
//
//
//    // Null safety check
//    private boolean canSetListener() {
//        return onItemClickListener != null && position != RecyclerView.NO_POSITION;
//    }
}
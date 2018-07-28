package com.example.raju.demoBlog.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.raju.demoBlog.R;
import com.example.raju.demoBlog.data.database.model.Item;

import java.util.List;

class ItemViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ItemViewHolder.class.getSimpleName();
    private final TextView titleView;
    private final TextView[] tagsView = new TextView[2];
    private int position;
    private long apiId;
    private String callId;

    ItemViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.item_title);
        tagsView[0] = itemView.findViewById(R.id.tag0);
        tagsView[1] = itemView.findViewById(R.id.tag1);

        titleView.setOnClickListener(titleView -> {
//            Context context = titleView.getContext();
//            Intent intent = new Intent(context, DetailActivity.class);
//            intent.putExtra(AppIE$Utils.EXTRA_API_ID, apiId);
//            intent.putExtra(AppIE$Utils.EXTRA_CALL_ID, callId);
//            // Important to check if intent can resolve the activity
//            if (intent.resolveActivity(context.getPackageManager()) != null)
//                context.startActivity(intent);
        });
        // todo: implement tag clickListener
    }

    void bind(Item item) {
        // Get the position while binding
        position = getAdapterPosition();

        apiId = item.getItem_id();
        callId = item.getId();

        titleView.setText(item.getTitle());
        List<String> tags = item.getTags();
        if (tags != null && !tags.isEmpty()) {
            // Get the list size and set it to maximum 2
            int finalSize = tags.size() > 2 ? 2 : 1;
            for (int i = 0; i < finalSize; i++) {
                tagsView[i].setText(tags.get(i));
            }
        } else {
            tagsView[0].setText("Oops");
        }
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
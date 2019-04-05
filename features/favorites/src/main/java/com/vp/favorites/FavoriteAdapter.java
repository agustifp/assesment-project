package com.vp.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vp.database.model.entity.ListItem;
import com.vp.list.GlideApp;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ListViewHolder> {
    private static final String NO_IMAGE = "N/A";
    private List<ListItem> listItems = Collections.emptyList();
    private OnItemClickListener EMPTY_ON_ITEM_CLICK_LISTENER = imdbID -> {
        //empty listener
    };
    private OnItemClickListener onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        if (listItem.getPoster() != null && !NO_IMAGE.equals(listItem.getPoster())) {
            GlideApp
                    .with(holder.image)
                    .load(listItem.getPoster())
                    .centerCrop()
                    .into(holder.image);
            holder.textViewTitle.setText(listItem.getTitle());
        } else {
            holder.image.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setItems(List<ListItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    public void clearItems() {
        listItems.clear();
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener;
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView textViewTitle;
        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.poster);
            textViewTitle = itemView.findViewById(R.id.title);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(listItems.get(getAdapterPosition()).getImdbID());
        }
    }

    interface OnItemClickListener {
        void onItemClick(String imdbID);
    }
}
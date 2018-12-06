package com.bowoon.android.android_http_practice.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowoon.android.android_http_practice.R;
import com.bowoon.android.android_http_practice.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;
    private ItemClickListener listener;

    public RecyclerAdapter(List<Item> items, ItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public String getItemTitle(int position) {
        return items.get(position).getName().getFirst();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        holder = new UsersViewHolder(v, listener);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        ((UsersViewHolder) holder).name.setText(items.get(position).getName().getFirst());
        Picasso.get().load(items.get(position).getUserProfileImage().getThumbnail()).into(((UsersViewHolder) holder).profileImg);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView profileImg;

        private UsersViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.text);
            this.profileImg = (ImageView) itemView.findViewById(R.id.userImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
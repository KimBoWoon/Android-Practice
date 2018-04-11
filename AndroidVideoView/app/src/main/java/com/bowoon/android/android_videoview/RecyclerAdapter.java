package com.bowoon.android.android_videoview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

interface ItemClickListener {
    void onItemClick(int position);
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {
    private Context context;
    private List<Item> items;

    public RecyclerAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public String getItemTitle(int position) {
        return items.get(position).getTitle();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, getItemTitle(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("path", items.get(position).getPath());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        holder = new ItemViewHolder(v, this);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        ((ItemViewHolder) holder).getTitle().setText(items.get(position).getTitle());
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(items.get(position).getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        ((ItemViewHolder) holder).getThumbnail().setImageBitmap(thumbnail);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView thumbnail;

        public ItemViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);

            this.title = (TextView) itemView.findViewById(R.id.video_title);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public ImageView getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(ImageView thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}

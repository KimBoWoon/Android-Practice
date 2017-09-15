package com.example.secret.rxandroidhttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Secret on 2017-09-15.
 */

interface ItemClickListener {
    void onItemClick(int position);
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {
    private List<Item> items;
    private Context context;
//    private final int VIEW_TYPE_TEXT = 0;
//    private final int VIEW_TYPE_IMAGE = 1;

    public RecyclerAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public String getItemTitle(int position) {
        return items.get(position).getName().getFirst();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, getItemTitle(position), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position % 2 == 0 ? VIEW_TYPE_TEXT : VIEW_TYPE_IMAGE;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

//        if (viewType == VIEW_TYPE_TEXT) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//
//            holder = new TextViewHolder(v, this);
//        }
//        else if (viewType == VIEW_TYPE_IMAGE) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//
//            holder = new ImageViewHolder(v, this);
//        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        holder = new UsersViewHolder(v, this);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

//        if (holder instanceof TextViewHolder) {
//            ((TextViewHolder) holder).title.setText(items.get(position).getName());
//        }
//        else if (holder instanceof ImageViewHolder) {
//            ((ImageViewHolder) holder).image.setBackgroundResource(items.get(position).getImage());
//        }
        try {
            ((UsersViewHolder) holder).name.setText(items.get(position).getName().getFirst());
            Bitmap bitmap = BitmapFactory.decodeStream(items.get(position).getUserProfileImage().getThumbnail().openStream());
            ((UsersViewHolder) holder).profileImg.setImageBitmap(bitmap);
//        ((UsersViewHolder) holder).profileImg.setImageURI(items.get(position).getUserProfileImage().getThumbnail());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class UsersViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView profileImg;

        public UsersViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.text);
            this.profileImg = (ImageView) itemView.findViewById(R.id.userImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text);
        }

        public TextViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

//    static class ImageViewHolder extends RecyclerView.ViewHolder {
//        private ImageView image;
//        private CardView cardView;
//
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//            this.image = (ImageView) itemView.findViewById(R.id.image);
//            this.cardView = (CardView) itemView.findViewById(R.id.cardview);
//        }
//
//        public ImageViewHolder(View itemView, final ItemClickListener itemClickListener) {
//            super(itemView);
//            this.image = (ImageView) itemView.findViewById(R.id.image);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemClickListener.onItemClick(getAdapterPosition());
//                }
//            });
//        }
//    }
}
package com.bowoon.recyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 보운 on 2016-12-19.
 */


/**
 * 여러 타임의 ViewHolder가 있을 때 interface를 선언해서 사용하면 onCreateViewHolder의 크기가 커기는 것을 방지 할 수 잇다.
 * MVP pattern에서 Presenter 넣어서 Unit Test가 쉬워진다.
 */
interface ItemClickListener {
    void onItemClick(int position);
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {
    private Context context;
    private List<Item> items;
    private final int VIEW_TYPE_TEXT = 0;
    private final int VIEW_TYPE_IMAGE = 1;

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
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? VIEW_TYPE_TEXT : VIEW_TYPE_IMAGE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if (viewType == VIEW_TYPE_TEXT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);

            holder = new TextViewHolder(v, this);
        }
        else if (viewType == VIEW_TYPE_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);

            holder = new ImageViewHolder(v, this);
        }

        return holder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).title.setText(items.get(position).getTitle());
        }
        else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).image.setBackgroundResource(items.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    /** ViewHolder의 static class 의미
     *  nested class는 상위 클래스의 변수 및 함수들도 접근할 수 있는데
     *  이를 방지하기위해 static으로 선언해 static nested class로 만든다
    */
    static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CardView cardView;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text);
            this.cardView = (CardView) itemView.findViewById(R.id.cardview);
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

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CardView cardView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.cardView = (CardView) itemView.findViewById(R.id.cardview);
        }

        public ImageViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
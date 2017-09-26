package com.practice.volleypractice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.practice.volleypractice.PersonModel.Person;

import java.util.List;

/**
 * Created by Null on 2017-09-26.
 */

interface ItemClickListener {
    void onItemClick(int position);
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemClickListener {
    private List<Person> items;
    private Context context;

    public RecyclerAdapter(Context context, List<Person> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        holder = new UsersViewHolder(v, this);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        final ImageLoader il = VolleyPractice.getInstance(context).getImageLoader();

        ((UsersViewHolder) holder).name.setText(items.get(position).getName().getFirst());
        il.get(items.get(position).getUserProfileImage().getLarge(),
                ImageLoader.getImageListener(((UsersViewHolder) holder).profileImg,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, getItemTitle(position), Toast.LENGTH_SHORT).show();
    }

    public String getItemTitle(int position) {
        return items.get(position).getName().getFirst();
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
}

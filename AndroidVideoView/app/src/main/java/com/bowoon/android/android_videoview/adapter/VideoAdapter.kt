package com.bowoon.android.android_videoview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.listener.ItemClickListener
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.viewholder.VideoViewHolder

class VideoAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<VideoViewHolder>() {
    private var videos = ArrayList<Video>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
            VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false), listener)

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    fun setItems(videos: ArrayList<Video>) {
        this.videos = videos
        notifyDataSetChanged()
    }
}

package com.bowoon.android.android_videoview.viewholder

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.listener.ItemClickListener
import com.bowoon.android.android_videoview.model.Video
import kotlinx.android.synthetic.main.item_layout.view.*

class VideoViewHolder(private val view: View, val listener: ItemClickListener) : RecyclerView.ViewHolder(view) {
    fun bind(video: Video) {
        with(view) {
            ThumbnailUtils.createVideoThumbnail(video.path, MediaStore.Images.Thumbnails.MINI_KIND).let {
                videoThumbnail.setImageBitmap(it)
            }
            videoTitle.text = video.title
            setOnClickListener { view -> listener.onItemClick(video) }
        }
    }
}
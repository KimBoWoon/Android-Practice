package com.bowoon.android.android_videoview.adapter

import android.content.Intent
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.VideoItemBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.activites.VideoPlayerActivity

class VideoAdapter(private val videos: MutableList<Video>?) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
            VideoViewHolder(DataBindingUtil.inflate<VideoItemBinding>(LayoutInflater.from(parent.context), R.layout.video_item, parent, false))

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        videos?.get(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = videos?.size ?: 0

    companion object {
        @BindingAdapter("bind_image")
        @JvmStatic
        fun bindImage(view: ImageView, path: String?) {
            view.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND))
        }
    }

    inner class VideoViewHolder(private val binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) {
            binding.video = item
            binding.root.setOnClickListener {
                binding.root.context.startActivity(
                        Intent(binding.root.context, VideoPlayerActivity::class.java).apply {
                            putExtra("videoContent", item)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                )
            }
        }
    }
}

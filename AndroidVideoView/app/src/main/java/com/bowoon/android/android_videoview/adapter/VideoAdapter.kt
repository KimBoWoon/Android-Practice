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

interface SetOnVideoClickListener {
    fun onClick(video: Video)
}

class VideoAdapter(private val videos: MutableList<Video>?) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
            VideoViewHolder(DataBindingUtil.inflate<VideoItemBinding>(LayoutInflater.from(parent.context), R.layout.video_item, parent, false))

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        videos?.let {
            holder.binding.video = it[position]
            holder.binding.listener = object : SetOnVideoClickListener {
                override fun onClick(video: Video) {
                    Toast.makeText(holder.binding.root.context, video.title, Toast.LENGTH_SHORT).show()
                    val intent = Intent(holder.binding.root.context, VideoPlayerActivity::class.java)
                    intent.putExtra("videoContent", video)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    holder.binding.root.context.startActivity(intent)
                }
            }
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

    inner class VideoViewHolder(val binding: VideoItemBinding) : RecyclerView.ViewHolder(binding.root)
}

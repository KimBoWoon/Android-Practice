package com.bowoon.android.android_videoview.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.activites.VideoPlayerActivity
import com.bowoon.android.android_videoview.databinding.FolderViewholderBinding
import com.bowoon.android.android_videoview.databinding.VideoViewholderBinding
import com.bowoon.android.android_videoview.model.Video
import com.bowoon.android.android_videoview.utils.Utils

class FolderListAdapter(private val folderMap: MutableMap<String, MutableList<Video>>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val videoList = mutableListOf<Any>()

    init {
        folderMap?.toSortedMap()?.map { (key, value) ->
            videoList.add(key)
            value.sortBy { it.title }
            value.map {
                videoList.add(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FOLDER -> FolderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.folder_viewholder, parent, false))
            VIDEO -> VideoViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.video_viewholder, parent, false))
            else -> { throw IllegalAccessException("ViewHolder not found") }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FolderViewHolder -> holder.bind(videoList[position] as? String)
            is VideoViewHolder -> holder.bind(videoList[position] as? Video)
        }
    }

    override fun getItemCount(): Int = videoList.size

    override fun getItemViewType(position: Int): Int {
        return when (videoList[position]) {
            is String -> FOLDER
            is Video -> VIDEO
            else -> { throw IllegalAccessException("data not found") }
        }
    }

    inner class FolderViewHolder(private val binding: FolderViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String?) {
            binding.folderTitle.text = item
        }
    }

    inner class VideoViewHolder(private val binding: VideoViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video?) {
            binding.video = item
            binding.videoDuration.text = String.format("duration : %s", Utils.getTimeString(item?.duration?.toInt()))
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

    companion object {
        private const val FOLDER = 0
        private const val VIDEO = 1
    }
}
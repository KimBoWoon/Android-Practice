package com.bowoon.android.android_videoview.adapter

import android.content.Context
import android.content.Intent
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.video.VideoPlayerActivity
import com.bowoon.android.android_videoview.vo.Item

class RecyclerAdapter(private val context: Context, private val items: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemClickListener {
    fun getItemTitle(position: Int): String {
        return items[position].title
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(context, getItemTitle(position), Toast.LENGTH_SHORT).show()
        val intent = Intent(context, VideoPlayerActivity::class.java)
        intent.putExtra("videoContent", items[position])
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        holder = ItemViewHolder(v, this)

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).title.text = items[position].title
        val thumbnail = ThumbnailUtils.createVideoThumbnail(items[position].path, MediaStore.Images.Thumbnails.MINI_KIND)
        holder.thumbnail.setImageBitmap(thumbnail)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    internal class ItemViewHolder(itemView: View, itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<TextView>(R.id.video_title)
        var thumbnail: ImageView = itemView.findViewById<ImageView>(R.id.video_thumbnail)

        init {
            itemView.setOnClickListener { itemClickListener.onItemClick(adapterPosition) }
        }
    }
}

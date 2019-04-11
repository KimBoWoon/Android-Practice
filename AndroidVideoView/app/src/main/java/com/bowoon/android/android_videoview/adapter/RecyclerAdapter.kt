package com.bowoon.android.android_videoview.adapter

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.ItemLayoutBinding
import com.bowoon.android.android_videoview.vo.Item

class RecyclerAdapter(val listener: ItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<Item>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_layout,
                parent,
                false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            ThumbnailUtils.createVideoThumbnail(items!![position].path, MediaStore.Images.Thumbnails.MINI_KIND).let {
                holder.binding.videoThumbnail.setImageBitmap(it)
            }
            holder.binding.videoTitle.text = items!![position].title
            holder.binding.item = items!![position]
            holder.binding.listener = listener
        }
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    fun setItems(items: ArrayList<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object ViewHolder {
        class ItemViewHolder(var binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
    }
}

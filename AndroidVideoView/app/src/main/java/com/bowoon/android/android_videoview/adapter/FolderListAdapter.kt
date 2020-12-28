package com.bowoon.android.android_videoview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_videoview.R
import com.bowoon.android.android_videoview.databinding.FolderViewholderBinding

class FolderListAdapter(private val folderList: MutableList<String>?) : RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder =
            FolderViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.folder_viewholder, parent, false))

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        folderList?.get(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = folderList?.size ?: 0

    inner class FolderViewHolder(private val binding: FolderViewholderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.folderTitle.text = item
        }
    }
}
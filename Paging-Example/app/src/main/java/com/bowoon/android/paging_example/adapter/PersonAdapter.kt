package com.bowoon.android.paging_example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.databinding.PersonItemBinding
import com.bowoon.android.paging_example.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

class PersonAdapter : PagedListAdapter<Item, PersonAdapter.PersonViewHolder>(PERSON_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.person_item, parent, false))

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.binding.item = it }
    }

    companion object {
        @BindingAdapter("bind_image")
        @JvmStatic
        fun bindImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .centerCrop()
                .signature(ObjectKey(System.currentTimeMillis()))
                .into(view)
        }

        private val PERSON_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.name?.first == newItem.name?.first && oldItem.name?.last == newItem.name?.last

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }

    inner class PersonViewHolder(val binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)
}
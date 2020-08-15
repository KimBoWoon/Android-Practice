package com.bowoon.android.paging_example.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.databinding.PersonItemBinding
import com.bowoon.android.paging_example.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

class PersonAdapter(private val items: MutableList<Item>?) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.person_item, parent, false))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        items?.let { holder.binding.item = it[position] }
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
    }

    inner class PersonViewHolder(val binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)
}
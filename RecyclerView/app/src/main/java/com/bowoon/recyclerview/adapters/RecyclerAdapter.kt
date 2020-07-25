package com.bowoon.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bowoon.recyclerview.viewholders.ImageViewHolder
import com.bowoon.recyclerview.model.Item
import com.bowoon.recyclerview.R
import com.bowoon.recyclerview.viewholders.TextViewHolder

class RecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val VIEW_TYPE_TEXT = 0
    private val VIEW_TYPE_IMAGE = 1
    private var items = ArrayList<Item>()

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder: RecyclerView.ViewHolder = TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false))

        when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
                holder = ImageViewHolder(v)
            }
            VIEW_TYPE_TEXT -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview, parent, false)
                holder = TextViewHolder(v)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is TextViewHolder -> holder.bind(items[position])
            is ImageViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Item>) {
        this.items = items as ArrayList<Item>
        notifyDataSetChanged()
    }
}
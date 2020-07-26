package com.bowoon.android.android_retrofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_retrofit.R
import com.bowoon.android.android_retrofit.model.Item
import com.bowoon.android.android_retrofit.model.PersonModel
import com.bowoon.android.android_retrofit.viewholder.PersonViewHolder

class PersonAdapter : RecyclerView.Adapter<PersonViewHolder>() {
    private var items = ArrayList<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<Item>) {
        this.items = items as ArrayList<Item>
        notifyDataSetChanged()
    }
}
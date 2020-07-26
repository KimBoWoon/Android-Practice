package com.bowoon.android.android_retrofit.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.android_retrofit.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.item_view.view.*

class PersonViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
        with(view) {
            showUserImage(item.userProfileImage.thumbnail)
            userName.text = String.format("%s.%s %s", item.name.title, item.name.first, item.name.last)
        }
    }

    private fun showUserImage(url: String) {
        Glide.with(view.context)
            .load(url)
            .centerCrop()
            .signature(ObjectKey(System.currentTimeMillis()))
            .into(view.userImg)
    }
}
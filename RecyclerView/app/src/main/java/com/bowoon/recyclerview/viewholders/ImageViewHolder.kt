package com.bowoon.recyclerview.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.recyclerview.model.Item
import kotlinx.android.synthetic.main.item_cardview.view.*

class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Item) {
        with (view) {
//            text.text = item.title
            image.setImageResource(item.image)
        }
    }
}
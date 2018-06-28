package com.bowoon.android.kotlin

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class UserAdapter(val items: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.Holder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.name?.text = items[position].name["title"] + " " + items[position].name["first"] + " " + items[position].name["last"]
        Picasso.get().load(items[position].picture["medium"]).into(holder.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(v)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val name = itemView?.findViewById<TextView>(R.id.name)
        val image = itemView?.findViewById<ImageView>(R.id.user_image)
    }
}
package com.bowoon.android.hilt_practice.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.hilt_practice.list.ListType
import com.bowoon.android.hilt_practice.list.ViewHolderFactory

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    open var items: MutableList<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var activityVM: BaseViewModel? = null
    var fragmentVM: BaseViewModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolderFactory.createViewHolder(ListType.values()[viewType], parent, activityVM, fragmentVM)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items?.get(position)?.let {
            ViewHolderFactory.bindViewHolder(holder, position, items?.get(position))
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    fun getItem(position: Int): T? = items?.get(position)
    fun clearData() {
        items?.let {
            it.clear()
            notifyDataSetChanged()
        }
    }

    abstract override fun getItemViewType(position: Int): Int
}
package com.bowoon.android.hilt_practice.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.hilt_practice.R
import com.bowoon.android.hilt_practice.base.BaseViewModel
import com.bowoon.android.hilt_practice.model.Person
import com.bowoon.android.hilt_practice.viewholder.PersonViewHolder

object ViewHolderFactory {
    fun createViewHolder(type: ListType, parent: ViewGroup, activityVM: BaseViewModel? = null, fragmentVM: BaseViewModel? = null): RecyclerView.ViewHolder {
        return when (type) {
            ListType.PERSON -> {
                PersonViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.viewholder_person, parent, false))
            }
        }
    }
    fun <T> bindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: T?) {
        item?.let {
            when (holder) {
                is PersonViewHolder -> { holder.bind(item as Person) }
            }
        }
    }
}
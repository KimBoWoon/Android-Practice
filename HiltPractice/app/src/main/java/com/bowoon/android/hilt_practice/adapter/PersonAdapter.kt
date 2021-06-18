package com.bowoon.android.hilt_practice.adapter

import com.bowoon.android.hilt_practice.base.BaseRecyclerViewAdapter
import com.bowoon.android.hilt_practice.list.ListType
import com.bowoon.android.hilt_practice.model.Person

class PersonAdapter : BaseRecyclerViewAdapter<Person>() {
    override fun getItemViewType(position: Int) = ListType.PERSON.ordinal
}
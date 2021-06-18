package com.bowoon.android.hilt_practice.viewholder

import com.bowoon.android.hilt_practice.base.BaseViewHolder
import com.bowoon.android.hilt_practice.databinding.ViewholderPersonBinding
import com.bowoon.android.hilt_practice.model.Person

class PersonViewHolder(
    private val binding: ViewholderPersonBinding
) : BaseViewHolder(binding.root) {
    fun bind(item: Person) {
        binding.dto = item
    }
}
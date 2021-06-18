package com.bowoon.android.hilt_practice.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:loadImageUrl")
fun ImageView?.loadImageUrl(url: String?) {
    this?.let {
        if (!url.isNullOrEmpty()) {
            Glide
                .with(it)
                .load(url)
                .into(it)
        }
    }
}
package com.bowoon.android.hilt_practice.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val compositeDisposable = CompositeDisposable()
}
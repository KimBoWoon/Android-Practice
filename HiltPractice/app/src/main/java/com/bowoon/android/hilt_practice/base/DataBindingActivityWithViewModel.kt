package com.bowoon.android.hilt_practice.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class DataBindingActivityWithViewModel<V: ViewDataBinding, VM: ViewModel>(
    @LayoutRes private val layout: Int,
    private val viewModelClass: Class<VM>
) : AppCompatActivity() {
    protected lateinit var binding: V
    val activityVM: VM by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout)
    }

    abstract fun initLiveData()
    abstract fun initBinding()
}
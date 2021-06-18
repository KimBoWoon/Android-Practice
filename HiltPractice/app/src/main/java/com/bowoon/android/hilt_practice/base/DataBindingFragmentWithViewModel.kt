package com.bowoon.android.hilt_practice.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class DataBindingFragmentWithViewModel<V : ViewDataBinding, VM : ViewModel, ACT_VM : ViewModel>(
    @LayoutRes private val layoutId: Int,
    private val fragmentVMClass: Class<VM>,
    private val activityVMClass: Class<ACT_VM>
) : Fragment() {
    protected lateinit var binding: V
    val fragmentVM: VM by lazy {
        ViewModelProvider(this).get(fragmentVMClass)
    }
    val activityVM: ACT_VM by lazy {
        ViewModelProvider(this).get(activityVMClass)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    abstract fun initLiveData()
    abstract fun initBinding()
}
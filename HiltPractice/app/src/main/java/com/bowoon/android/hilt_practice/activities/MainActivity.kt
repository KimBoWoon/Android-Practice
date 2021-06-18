package com.bowoon.android.hilt_practice.activities

import android.os.Bundle
import com.bowoon.android.hilt_practice.R
import com.bowoon.android.hilt_practice.activities.viewmodel.MainActivityViewModel
import com.bowoon.android.hilt_practice.adapter.PersonAdapter
import com.bowoon.android.hilt_practice.base.DataBindingActivityWithViewModel
import com.bowoon.android.hilt_practice.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : DataBindingActivityWithViewModel<ActivityMainBinding, MainActivityViewModel>
    (R.layout.activity_main, MainActivityViewModel::class.java) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        lifecycle.addObserver(activityVM)

        initBinding()
        initLiveData()
    }

    override fun initBinding() {
        binding.rvPersonList.adapter = PersonAdapter()
    }

    override fun initLiveData() {
        activityVM.personList.observe(this) {
            (binding.rvPersonList.adapter as? PersonAdapter)?.let { adapter ->
                adapter.items = it
                adapter.notifyDataSetChanged()
            }
        }
    }
}
package com.bowoon.android.paging_example.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.ActivityMainBinding
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.viewmodels.PersonViewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val viewModel by lazy {
//        ViewModelProvider(this,
//            ViewModelFactory(
//                application
//            )
//        ).get(PersonViewModel::class.java)
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PersonViewModel::class.java)
    }
    private val adapter = PersonAdapter()

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
    }

    private fun initAdapter() {
        binding.rvPersonList.adapter = adapter
        viewModel.userList.observe(this, Observer<PagedList<Item>> {
            Log.d(TAG, it.toString())
            adapter.submitList(it)
        })
    }
}
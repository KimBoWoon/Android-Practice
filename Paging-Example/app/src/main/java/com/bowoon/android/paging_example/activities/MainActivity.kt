package com.bowoon.android.paging_example.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bowoon.android.paging_example.R
//import com.bowoon.android.paging_example.adapter.ViewPagerAdapter
import com.bowoon.android.paging_example.adapter.ViewPagerRecyclerViewAdapter
import com.bowoon.android.paging_example.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val tabs = mutableListOf<Tab>(
        Tab(""),
        Tab("male"),
        Tab("female")
    )
    companion object {
        const val TAG = "MainActivity"
    }

    class Tab(val gender: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.bottomNavigationBar.setupWithNavController((supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment).navController)

        binding.viewPager2.adapter = ViewPagerRecyclerViewAdapter(tabs)

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            if (tabs[position].gender.isBlank()) {
                tab.text = "ALL"
            } else {
                tab.text = tabs[position].gender
            }
        }.attach()
    }
}
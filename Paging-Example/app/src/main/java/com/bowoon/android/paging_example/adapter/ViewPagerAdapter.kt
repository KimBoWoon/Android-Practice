package com.bowoon.android.paging_example.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bowoon.android.paging_example.fragments.PersonFragment

class ViewPagerAdapter(manager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(manager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> { PersonFragment.newInstance("") }
            1 -> { PersonFragment.newInstance("male") }
            2 -> { PersonFragment.newInstance("female") }
            else -> { throw IllegalAccessException("unknown fragment") }
        }
    }
}
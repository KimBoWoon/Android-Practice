package com.bowoon.android.paging_example.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.activities.MainActivity
import com.bowoon.android.paging_example.views.PersonView

class ViewPagerRecyclerViewAdapter(private val tabs: MutableList<MainActivity.Tab>) : RecyclerView.Adapter<ViewPagerRecyclerViewAdapter.FragmentViewHolder>() {
    class FragmentViewHolder private constructor(container: FrameLayout) : RecyclerView.ViewHolder(container) {
        val container: FrameLayout
            get() = itemView as FrameLayout

        companion object {
            fun create(newInstance: FrameLayout): FragmentViewHolder {
                newInstance.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                newInstance.id = ViewCompat.generateViewId()
                return FragmentViewHolder(newInstance)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder =
        FragmentViewHolder.create(PersonView(parent.context, tabs[viewType]))

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {}

    override fun getItemCount(): Int = tabs.size

    override fun getItemViewType(position: Int): Int = position
}
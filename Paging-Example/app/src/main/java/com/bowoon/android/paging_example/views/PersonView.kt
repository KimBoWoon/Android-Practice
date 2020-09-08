package com.bowoon.android.paging_example.views

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.activities.MainActivity
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.FragmentItemLayoutBinding
import com.bowoon.android.paging_example.utils.PaginationStatus
import com.bowoon.android.paging_example.utils.dp
import com.bowoon.android.paging_example.viewmodels.PersonViewModel
import io.reactivex.rxkotlin.addTo

class PersonView(context: Context, tab: MainActivity.Tab) : FrameLayout(context) {
    private val binding by lazy {
        DataBindingUtil.inflate<FragmentItemLayoutBinding>(LayoutInflater.from(context), R.layout.fragment_item_layout, this, true)
    }
    private val viewModel by lazy {
        ViewModelProvider(context as FragmentActivity, ViewModelProvider.NewInstanceFactory()).get(PersonViewModel::class.java)
    }
    private val adapter = PersonAdapter()

    companion object {
        const val TAG = "PersonView"
    }

    init {
        binding.rvPersonList.adapter = adapter
        binding.rvPersonList.setDividerDecorator()
        viewModel.add(tab.gender)
        viewModel.getPersonData(tab.gender)?.subscribe(
            { adapter.submitList(it) },
            { e -> e.printStackTrace() },
            { Log.v(TAG, "Done") }
        )?.addTo(viewModel.compositeDisposable)
        viewModel.getPaginationState().observe(context as FragmentActivity, Observer {
            when (it) {
                PaginationStatus.Loading -> {
                    binding.pbHttpRequest.visibility = VISIBLE
                }
                PaginationStatus.Empty -> {
                    binding.pbHttpRequest.visibility = GONE
                }
                PaginationStatus.NotEmpty -> {
                    binding.pbHttpRequest.visibility = GONE
                }
            }
        })
    }

    private fun RecyclerView.setDividerDecorator() {
        addItemDecoration(object : DividerItemDecoration(context, RecyclerView.VERTICAL) {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.bottom = 10.dp
            }
        }.apply{
            setDrawable(ColorDrawable(Color.parseColor("#dddddd")))
        })
    }
}
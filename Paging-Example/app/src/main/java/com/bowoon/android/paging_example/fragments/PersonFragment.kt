package com.bowoon.android.paging_example.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.FragmentItemLayoutBinding
import com.bowoon.android.paging_example.utils.PaginationStatus
import com.bowoon.android.paging_example.viewmodels.PersonViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_item_layout.view.*

class PersonFragment(private val gender: String) : Fragment() {
    private lateinit var binding: FragmentItemLayoutBinding
    private val adapter = PersonAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PersonViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "UserFragment"

        fun newInstance(gender: String): Fragment {
            return PersonFragment(gender)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_item_layout,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.rv_person_list.adapter = adapter
        view.rv_person_list.setDividerDecorator()
        viewModel.add(gender)
        viewModel.getPersonData(gender).subscribe(
            { adapter.submitList(it) },
            { e -> e.printStackTrace() },
            { Log.v(TAG, "Done") }
        ).addTo(compositeDisposable)
        viewModel.getPaginationState().observe(viewLifecycleOwner, Observer {
            when (it) {
                PaginationStatus.Loading -> {
                    view.pb_http_request.visibility = View.VISIBLE
                }
                PaginationStatus.Empty -> {
                    view.pb_http_request.visibility = View.GONE
                }
                PaginationStatus.NotEmpty -> {
                    view.pb_http_request.visibility = View.GONE
                }
            }
        })
    }

    private fun RecyclerView.setDividerDecorator() {
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                outRect.bottom = 10
            }
        })
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
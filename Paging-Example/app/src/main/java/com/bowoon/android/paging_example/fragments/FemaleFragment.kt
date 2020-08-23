package com.bowoon.android.paging_example.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.FemaleFragmentBinding
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.viewmodels.FemaleViewModel
import kotlinx.android.synthetic.main.female_fragment.view.*

class FemaleFragment : Fragment() {
    private lateinit var binding: FemaleFragmentBinding
    private val adapter = PersonAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FemaleViewModel::class.java)
    }

    companion object {
        const val TAG = "FemaleFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.female_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.rv_person_list.adapter = adapter
        viewModel.userList.observe(viewLifecycleOwner, Observer<PagedList<Item>> {
            adapter.submitList(it)
        })
    }
}
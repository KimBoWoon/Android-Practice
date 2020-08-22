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
import androidx.navigation.Navigation
import androidx.paging.PagedList
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.MaleFragmentBinding
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.viewmodels.MaleViewModel
import kotlinx.android.synthetic.main.male_fragment.view.*

class MaleFragment : Fragment() {
    private lateinit var binding: MaleFragmentBinding
    private val adapter = PersonAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MaleViewModel::class.java)
    }

    companion object {
        const val TAG = "MaleFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.male_fragment, container, false)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.male_fragment,
            container,
            false
        )
        view.userFragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.user)
        }
        view.maleFragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.male)
        }
        view.femaleFragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.female)
        }
        view.rv_person_list.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.userList.observe(viewLifecycleOwner, Observer<PagedList<Item>> {
            Log.d(TAG, it.toString())
            adapter.submitList(it)
        })
    }
}
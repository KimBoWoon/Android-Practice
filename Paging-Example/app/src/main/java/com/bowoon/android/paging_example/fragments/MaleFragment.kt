package com.bowoon.android.paging_example.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.MaleFragmentBinding
import com.bowoon.android.paging_example.viewmodels.MaleViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.male_fragment.view.*

class MaleFragment : Fragment() {
    private lateinit var binding: MaleFragmentBinding
    private val adapter = PersonAdapter()
    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MaleViewModel::class.java)
    }
    private val compositeDisposable = CompositeDisposable()

    companion object {
        const val TAG = "MaleFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.male_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.rv_person_list.adapter = adapter
        viewModel.getMaleData().subscribe(
            { adapter.submitList(it) },
            { e -> e.printStackTrace() },
            { Log.v(TAG, "Done") }
        ).addTo(compositeDisposable)
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
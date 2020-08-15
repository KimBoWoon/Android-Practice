package com.bowoon.android.paging_example.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.android.paging_example.R
import com.bowoon.android.paging_example.adapter.PersonAdapter
import com.bowoon.android.paging_example.databinding.ActivityMainBinding
import com.bowoon.android.paging_example.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Repository
            .useRx()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { binding.rvPersonList.adapter = PersonAdapter(it.items) },
                { e -> e.printStackTrace() }
            ).addTo(CompositeDisposable())
    }
}
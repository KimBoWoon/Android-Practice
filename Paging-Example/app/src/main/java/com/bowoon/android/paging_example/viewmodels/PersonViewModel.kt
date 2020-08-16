package com.bowoon.android.paging_example.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bowoon.android.paging_example.adapter.paging.PersonDataFactory
import com.bowoon.android.paging_example.model.Item
import io.reactivex.disposables.CompositeDisposable

class PersonViewModel : ViewModel() {
    var userList: LiveData<PagedList<Item>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 15
    private val sourceFactory: PersonDataFactory = PersonDataFactory(compositeDisposable)

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2) // default: page size * 3
            .setPrefetchDistance(10) // default: page size
            .setEnablePlaceholders(false) // default: true
            .build()

        userList = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
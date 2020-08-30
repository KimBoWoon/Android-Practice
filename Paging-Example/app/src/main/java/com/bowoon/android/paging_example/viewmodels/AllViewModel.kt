package com.bowoon.android.paging_example.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory.Companion.ALL
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AllViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 15
    private val paginationStatus = MutableLiveData<PaginationStatus>()
    private val factory = RandomUserDataFactory(compositeDisposable, paginationStatus, ALL)
    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2) // default: page size * 3
        .setPrefetchDistance(10) // default: page size
        .setEnablePlaceholders(true) // default: true
        .build()
    private val all = RxPagedListBuilder(factory, pagedListConfig)
        .setFetchScheduler(Schedulers.io())
        .setNotifyScheduler(AndroidSchedulers.mainThread())
        .buildFlowable(BackpressureStrategy.BUFFER)

    fun getAllData(): Flowable<PagedList<Item>> {
        return all
    }

    fun getPaginationState(): MutableLiveData<PaginationStatus> {
        return paginationStatus
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
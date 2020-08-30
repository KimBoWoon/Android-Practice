package com.bowoon.android.paging_example.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory.Companion.MALE
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MaleViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 15
    private val paginationStatus = MutableLiveData<PaginationStatus>()
    private val factory = RandomUserDataFactory(compositeDisposable, paginationStatus, MALE)
    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2) // default: page size * 3
        .setPrefetchDistance(10) // default: page size
        .setEnablePlaceholders(true) // default: true
        .build()
    private val male = RxPagedListBuilder(factory, pagedListConfig)
        .setFetchScheduler(Schedulers.io())
        .setNotifyScheduler(AndroidSchedulers.mainThread())
        .buildFlowable(BackpressureStrategy.BUFFER)

    fun getMaleData(): Flowable<PagedList<Item>> {
        return male
    }

    fun getPaginationState(): MutableLiveData<PaginationStatus> {
        return paginationStatus
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
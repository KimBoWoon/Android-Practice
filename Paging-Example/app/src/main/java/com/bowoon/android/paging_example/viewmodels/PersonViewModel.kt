package com.bowoon.android.paging_example.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.bowoon.android.paging_example.paging.PersonDataSource
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

class PersonViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    private val paginationStatus = MutableLiveData<PaginationStatus>()
    private val personMap = mutableMapOf<String, Flowable<PagedList<Item>>>()

    fun add(gender: String) {
        if (personMap[gender] == null) {
            personMap[gender] = PersonDataSource.create(gender, compositeDisposable, paginationStatus)
        }
    }

    fun getPersonData(gender: String): Flowable<PagedList<Item>>? {
        if (personMap[gender] != null) {
            return personMap[gender]
        }

        return null
    }

    fun getPaginationState(): MutableLiveData<PaginationStatus> {
        return paginationStatus
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
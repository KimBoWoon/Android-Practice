package com.bowoon.android.paging_example.adapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.disposables.CompositeDisposable

class RandomUserDataFactory(
    private val compositeDisposable: CompositeDisposable,
    private val paginationStatus: MutableLiveData<PaginationStatus>,
    private val gender: String
) : DataSource.Factory<Int, Item>() {
    private val mutableLive: MutableLiveData<DataSource<Int, Item>> = MutableLiveData<DataSource<Int, Item>>()
    private var personSource: DataSource<Int, Item>? = null

    companion object {
        const val ALL = "all"
        const val FEMALE = "female"
        const val MALE = "male"
    }

    override fun create(): DataSource<Int, Item> {
        return when (gender) {
            ALL -> {
                personSource = AllSource(compositeDisposable, paginationStatus)
                mutableLive.postValue(personSource)
                personSource!!
            }
            FEMALE -> {
                personSource = FemaleSource(compositeDisposable, paginationStatus)
                mutableLive.postValue(personSource)
                personSource!!
            }
            MALE -> {
                personSource = MaleSource(compositeDisposable, paginationStatus)
                mutableLive.postValue(personSource)
                personSource!!
            }
            else -> throw ClassNotFoundException("$gender is not found")
        }
    }
}
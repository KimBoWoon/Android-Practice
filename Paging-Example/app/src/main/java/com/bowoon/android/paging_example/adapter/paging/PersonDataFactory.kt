package com.bowoon.android.paging_example.adapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.bowoon.android.paging_example.model.Item
import io.reactivex.disposables.CompositeDisposable

class PersonDataFactory(private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Item>() {
    private val mutableLiveData: MutableLiveData<PersonDataSource> = MutableLiveData<PersonDataSource>()
    private var personDataSource: PersonDataSource? = null

    override fun create(): DataSource<Int, Item> {
        personDataSource = PersonDataSource(compositeDisposable)
        mutableLiveData.postValue(personDataSource)
        return personDataSource!!
    }
}
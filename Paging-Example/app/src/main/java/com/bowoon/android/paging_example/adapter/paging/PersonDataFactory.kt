package com.bowoon.android.paging_example.adapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.utils.ifNotNull
import com.bowoon.android.paging_example.utils.ifNull
import io.reactivex.disposables.CompositeDisposable

class PersonDataFactory(private val compositeDisposable: CompositeDisposable, private val gender: String) : DataSource.Factory<Int, Item>() {
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
                personSource = PersonSource(compositeDisposable)
                mutableLive.postValue(PersonSource(compositeDisposable))
                personSource ifNotNull {
                    personSource
                }
            }
            FEMALE -> {
                personSource = FemaleSource(compositeDisposable)
                mutableLive.postValue(FemaleSource(compositeDisposable))
                personSource ifNotNull {
                    personSource
                }
            }
            MALE -> {
                personSource = MaleSource(compositeDisposable)
                mutableLive.postValue(MaleSource(compositeDisposable))
                personSource ifNotNull {
                    personSource
                }
            }
            else -> throw ClassNotFoundException("$gender is not found")
        }
    }
}
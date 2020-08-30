package com.bowoon.android.paging_example.adapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory.Companion.FEMALE
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.network.provider.providePersonApi
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.disposables.CompositeDisposable

class FemaleSource(
    private val compositeDisposable: CompositeDisposable,
    private val paginationStatus: MutableLiveData<PaginationStatus>
) : PageKeyedDataSource<Int, Item>() {
    companion object {
        const val TAG = "FemaleSource"
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        compositeDisposable
            .add(providePersonApi()
                .getFemale(1, params.requestedLoadSize, FEMALE)
                .subscribe(
                    { users ->
                        when {
                            users == null -> {
                                paginationStatus.postValue(PaginationStatus.Empty)
                            }
                            users.items?.isEmpty()!! -> {
                                paginationStatus.postValue(PaginationStatus.Empty)
                            }
                            else -> {
                                paginationStatus.postValue(PaginationStatus.NotEmpty)
                                callback.onResult(users.items, null, 2)
                            }
                        }
                    },
                    { e -> e.printStackTrace() }
                )
            )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        compositeDisposable
            .add(providePersonApi()
                .getFemale(params.key, params.requestedLoadSize, FEMALE)
                .subscribe(
                    { users ->
                        when {
                            users == null -> {
                                paginationStatus.postValue(PaginationStatus.Empty)
                            }
                            users.items?.isEmpty()!! -> {
                                paginationStatus.postValue(PaginationStatus.Empty)
                            }
                            else -> {
                                paginationStatus.postValue(PaginationStatus.NotEmpty)
                                callback.onResult(users.items, params.key + 1)
                            }
                        }
                    },
                    { e -> e.printStackTrace() }
                )
            )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        TODO("Not yet implemented")
    }
}
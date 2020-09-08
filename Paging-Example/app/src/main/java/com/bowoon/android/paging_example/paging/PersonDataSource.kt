package com.bowoon.android.paging_example.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.network.provider.providePersonApi
import com.bowoon.android.paging_example.utils.PaginationStatus
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PersonDataSource(
    private val gender: String,
    private val compositeDisposable: CompositeDisposable,
    private val paginationStatus: MutableLiveData<PaginationStatus>
) : PageKeyedDataSource<Int, Item>() {
    companion object {
        const val TAG = "DataSource"
        private const val PAGE_SIZE = 20

        private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2) // default: page size * 3
            .setPrefetchDistance(10) // default: page size
            .setEnablePlaceholders(true) // default: true
            .build()

        fun create(gender: String, compositeDisposable: CompositeDisposable, paginationStatus: MutableLiveData<PaginationStatus>): Flowable<PagedList<Item>> {
            return RxPagedListBuilder(object : DataSource.Factory<Int, Item>() {
                override fun create(): DataSource<Int, Item> {
                    return PersonDataSource(gender, compositeDisposable, paginationStatus)
                }
            }, config)
                .setFetchScheduler(Schedulers.io())
                .setNotifyScheduler(AndroidSchedulers.mainThread())
                .buildFlowable(BackpressureStrategy.BUFFER)
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        compositeDisposable
            .add(providePersonApi()
                .getUsers(1, params.requestedLoadSize, gender)
                .doOnSubscribe {
                    paginationStatus.postValue(PaginationStatus.Loading)
                }
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
                .getUsers(params.key, params.requestedLoadSize, gender)
                .doOnSubscribe {
                    paginationStatus.postValue(PaginationStatus.Loading)
                }
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
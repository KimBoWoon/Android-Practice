package com.bowoon.android.paging_example.adapter.paging

import androidx.paging.PageKeyedDataSource
import com.bowoon.android.paging_example.adapter.paging.RandomUserDataFactory.Companion.FEMALE
import com.bowoon.android.paging_example.model.Item
import com.bowoon.android.paging_example.network.provider.providePersonApi
import io.reactivex.disposables.CompositeDisposable

class FemaleSource(private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Item>() {
    companion object {
        const val TAG = "FemaleSource"
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Item>) {
        val curPage = 1
        val nextPage = curPage + 1

        compositeDisposable
            .add(providePersonApi()
                .getFemale(curPage, params.requestedLoadSize, FEMALE)
                .subscribe(
                    { users -> callback.onResult(users.items!!, null, nextPage) },
                    { e -> e.printStackTrace() }
                )
            )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        compositeDisposable
            .add(providePersonApi()
                .getFemale(params.key, params.requestedLoadSize, FEMALE)
                .subscribe(
                    { users -> callback.onResult(users.items!!, params.key + 1) },
                    { e -> e.printStackTrace() }
                )
            )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        TODO("Not yet implemented")
    }
}
package com.bowoon.android.paging_example.adapter.paging

import androidx.paging.PageKeyedDataSource
import com.bowoon.android.paging_example.model.PersonModel

class PersonDataPaging : PageKeyedDataSource<Int, PersonModel>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PersonModel>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PersonModel>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PersonModel>) {
        TODO("Not yet implemented")
    }
}